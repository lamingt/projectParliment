import { CommentsInfoType } from "@/types";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import usePromise from "react-promise-suspense";
import { getAvatar } from "@/api";
import { formatDistanceToNow } from "date-fns";

interface CommentProps {
  commentInfo: CommentsInfoType;
}

function Comment({ commentInfo }: CommentProps) {
  const avatar = usePromise(getAvatar, [commentInfo.creatorInfo.userId]);

  return (
    <>
      <div className="flex items-center align gap-2 mt-5">
        <Avatar>
          <AvatarImage src={`/api/v1/user/avatar/${commentInfo.creatorInfo.userId}`} />
          <AvatarFallback>Avatar</AvatarFallback>
        </Avatar>
        <span className="font-semibold text-lg">{commentInfo.creatorInfo.username}</span> -{" "}
        {formatDistanceToNow(new Date(commentInfo.createdAt), { addSuffix: true })}
        {/* {commentInfo.text} */}
      </div>
      <div className="ml-[48px]">{commentInfo.text}</div>
    </>
  );
}

export default Comment;
