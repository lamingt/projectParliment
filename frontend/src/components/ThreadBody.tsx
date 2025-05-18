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
    <div className="p-5 flex flex-col gap-5 w-[100%]">
      <div className="text-3xl border-b-2 border-gray-300">{threadInfo.title}</div>
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
      <div className="flex flex-row gap-5">
        <div>Likes: {threadInfo.numLikes}</div>
        <div>Comments: {threadInfo.numComments}</div>
      </div>
    </div>
  );
}

export default ThreadBody;
