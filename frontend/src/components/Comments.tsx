import { getRootComments } from "@/api";
import { CommentsInfoType, CommentsResponseType, ThreadInfoType } from "@/types";
import { useState } from "react";
import usePromise from "react-promise-suspense";
import { useLocation } from "react-router";
import Comment from "./Comment";

function Comments() {
  const [pageNum, setPageNum] = useState(1);
  const [sort, setSort] = useState("likes");
  const location = useLocation();
  const threadInfo: ThreadInfoType = location.state;
  const data: CommentsResponseType = usePromise(
    getRootComments,
    [threadInfo.id, pageNum, sort],
    0.01
  );

  return (
    <div className="mt-[15px]">
      {data.commentInfo.map((comment: CommentsInfoType, _) => (
        // <div key={idx}>{comment.text}</div>
        <Comment commentInfo={comment} key={comment.id} />
      ))}
    </div>
  );
}

export default Comments;
