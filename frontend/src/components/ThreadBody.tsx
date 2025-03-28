import { useLocation } from "react-router";
import { ThreadInfoType } from "../types";

function ThreadBody() {
  const location = useLocation();
  const threadInfo: ThreadInfoType = location.state;
  const date = new Date(threadInfo.date);
  const formattedDate = date.toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
  return (
    <div className="flex flex-col min-w-[40vw] p-5 gap-5 border-r-2">
      <div className="text-3xl border-b-2">{threadInfo.title}</div>
      <div className="text-xl">Proposed: {formattedDate}</div>
      <div className="text-xl">Status: {threadInfo.status}</div>
      {threadInfo.active ? (
        <div className="text-xl text-green-600">Active</div>
      ) : (
        <div className="text-xl text-red-600">Inactive</div>
      )}
      {threadInfo.summary.trim() !== "No summary exists." && (
        <div>
          <p>Offical Summary:</p>
          <p>{threadInfo.summary}</p>
        </div>
      )}
      <div>
        <p>AI Summary:</p>
        <p>TODO!</p>
      </div>
    </div>
  );
}

export default ThreadBody;
