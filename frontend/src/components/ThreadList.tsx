import { Suspense, useEffect } from "react";
import usePromise from "react-promise-suspense";
import { getThreadList } from "../api";
import { useNavigate, useParams } from "react-router";

type threadInfoType = {
  id: string;
  title: string;
  summary: string;
  date: string;
  chamber: string;
  active: boolean;
  numLikes: number;
  numComments: number;
};

function ThreadList() {
  const { pageNum = "1" } = useParams<{ pageNum?: string }>();
  const data = usePromise(getThreadList, [pageNum]);
  const navigate = useNavigate();

  const handleThreadClick = (threadInfo: threadInfoType) => {
    navigate(`/thread/${threadInfo.title.replaceAll(" ", "-")}`, { state: threadInfo });
  };

  const handleNextClick = () => {
    navigate(`/page/${parseInt(pageNum) + 1}`);
  };

  const handlePrevClick = () => {
    navigate(`/page/${parseInt(pageNum) - 1}`);
  };

  return (
    <div className="flex flex-col justify-center items-center min-h-[96vh]">
      <div className="border border-black min-w-[40vw]">
        {data.data.pageInfo.map((threadInfo: threadInfoType) => (
          <div
            className="flex justify-between border cursor-pointer"
            key={threadInfo.title}
            onClick={() => handleThreadClick(threadInfo)}
          >
            <div>
              <p>{threadInfo.title}</p>
              <p>
                {threadInfo.date} <span>{threadInfo.active ? "active" : "inactive"}</span>
              </p>
            </div>
            <div>
              <p>{`Likes: ${threadInfo.numLikes}`}</p>
              <p>{`Comments: ${threadInfo.numComments}`}</p>
            </div>
            {/* <p>{JSON.stringify(threadInfo)}</p> */}
          </div>
        ))}
      </div>
      <div>
        {parseInt(pageNum) > 1 && <div onClick={handlePrevClick}>Back</div>}
        {data.data.pagination.currentPage < data.data.pagination.totalPages && (
          <div onClick={handleNextClick}>Next</div>
        )}
      </div>
    </div>
  );
}

export default ThreadList;
