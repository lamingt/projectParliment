import { Suspense } from "react";
import Header from "../components/Header";
import ThreadList from "../components/ThreadList";

function Home() {
  return (
    <div>
      <Suspense fallback={<div>Loading...</div>}>
        <ThreadList></ThreadList>
      </Suspense>
    </div>
  );
}

export default Home;
