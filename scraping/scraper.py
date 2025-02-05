import sys
import bs4 as BeautifulSoup
import requests
import psycopg2
import re

if __name__ == "__main__":
    limit = 100
    bills_info = []
    for i in range(1, limit):
        res = requests.get(f"https://www.aph.gov.au/Parliamentary_Business/Bills_Legislation/Bills_before_Parliament?Page={i}")
        soup = BeautifulSoup.BeautifulSoup(res.text, 'html.parser')
        
        # No more results
        if (soup.prettify().find("<ul class=\"search-filter-results\">") == -1):
            break 
        
        # print(f"--- Page {i} ---")
        # print("")

        li_elements = soup.find_all('li', recursive=True)
        bills = [li for li in li_elements if li.find('div')]
        for bill in bills:
            title = bill.select_one("h4 a").text.strip()
            date = bill.select_one("dl dd:nth-of-type(1)").text.strip()
            chamber = bill.select_one("dl dd:nth-of-type(2)").text.strip()
            status = bill.select_one("dl dd:nth-of-type(3)").text.strip()
            summary = bill.select_one("dl dd:nth-of-type(5)")
            summary = summary.text.strip() if summary is not None else "No summary exists."
            
            # print(f"Title: {title}")
            # print(f"Date: {date}")
            # print(f"Chamber: {chamber}")
            # print(f"Status: {status}")
            # print(f"Summary: {summary}")
            bills_info.append((title, date, chamber, status, summary))
        
        # print("")
    
    try:
        db = psycopg2.connect(user="postgres",
                              password="password",
                              host="127.0.0.1",
                              port="5432",
                              database="parliament")
        cursor = db.cursor()
        

        extension_query = """
            create extension if not exists "pgcrypto"
        """
        id_query = """
            alter table
                threads
            alter column
                id
            set default
                gen_random_uuid()
        """
        cursor.execute(extension_query)
        cursor.execute(id_query)
        
        insert_query = """
            insert into
                threads(title, date, chamber, status, summary, active)
            values
                (%s,%s,%s,%s,%s,%s)
            on conflict
                (title)
            do nothing
        """

        find_active_query = """
            select
                t.title
            from
                threads t
            where
                t.active = TRUE
        """

        cursor.executemany(insert_query, [(bill[0], bill[1], bill[2], bill[3], bill[4], True) for bill in bills_info])
        
        cursor.execute(find_active_query)
        stored_active = {row[0] for row in cursor.fetchall()}
        current_active = {bill[0] for bill in bills_info}
        new_inactive = stored_active - current_active
        
        if new_inactive:
            update_query = """
                update
                    threads
                set
                    active = FALSE
                where
                    title = ANY(%s)
            """
            cursor.execute(update_query, (list(new_inactive),))

        db.commit()
    except Exception as err:
        print("DB error: ", err)
    finally:
        if db:
            db.close()

        
            