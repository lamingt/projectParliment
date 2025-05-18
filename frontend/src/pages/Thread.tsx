import { useLocation } from "react-router";
import { ThreadInfoType } from "../types";
import ThreadBody from "../components/ThreadBody";
import CommentCreate from "../components/CommentCreate";
import { useContext, useState } from "react";
import { AuthContext } from "@/context/AuthContext";
import Comments from "@/components/Comments";

function Thread() {
  const location = useLocation();
  const threadInfo: ThreadInfoType = location.state;
  const [refreshKey, setRefreshKey] = useState(0);
  const { token } = useContext(AuthContext);

  const refreshComments = () => {
    setRefreshKey((key) => key + 1);
  };

  return (
    <div className="flex flex-col min-h-[96vh] sm:w-[90vw] md:w-[70vw] lg:w-[60vw] xl:w-[50vw] bg-gray-100 mx-auto">
      <ThreadBody />
      <div className="p-5">
        {token && <CommentCreate refreshComments={refreshComments} />}
        <Comments key={refreshKey} />
      </div>
      {/* <div>{JSON.stringify(threadInfo)}</div> */}
    </div>
  );
}

export default Thread;
