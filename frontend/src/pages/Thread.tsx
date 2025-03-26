import { useLocation } from "react-router";
import { ThreadInfoType } from "../types";
import ThreadBody from "../components/ThreadBody";

function Thread() {
  const location = useLocation();
  const threadInfo: ThreadInfoType = location.state;

  return (
    <div className="flex flex-row min-h-[96vh] max-w-[70vw] bg-slate-500 mx-auto">
      <ThreadBody />
      <div>{JSON.stringify(threadInfo)}</div>
    </div>
  );
}

export default Thread;
