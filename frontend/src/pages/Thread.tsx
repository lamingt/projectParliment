import { useLocation } from "react-router";
import { ThreadInfoType } from "../types";
import ThreadBody from "../components/ThreadBody";
import CommentCreate from "../components/CommentCreate";

function Thread() {
  const location = useLocation();
  const threadInfo: ThreadInfoType = location.state;

  return (
    <div className="flex flex-col min-h-[96vh] max-w-[50vw] bg-gray-100 mx-auto">
      <ThreadBody />
      <div className="p-5">
        <CommentCreate />
      </div>
      {/* <div>{JSON.stringify(threadInfo)}</div> */}
    </div>
  );
}

export default Thread;
