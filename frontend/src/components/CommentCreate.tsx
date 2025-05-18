import { useContext, useState } from "react";
import { Textarea } from "./ui/textarea";
import clsx from "clsx";
import { createComment } from "@/api";
import { AuthContext } from "@/context/AuthContext";
import { ThreadInfoType } from "@/types";
import { useLocation } from "react-router";

type CommontCreateProps = {
  refreshComments: () => void;
};

function CommentCreate({ refreshComments }: CommontCreateProps) {
  const location = useLocation();
  const [isClicked, setIsClicked] = useState(false);
  const [text, setText] = useState("");
  const { token } = useContext(AuthContext);
  const threadInfo: ThreadInfoType = location.state;

  const textAreaStyle = clsx({
    "w-[80%]": true,
    "border-black": true,
    "h-[2vh]": !isClicked,
    "h-[10vh]": isClicked,
    "placeholder:text-xl": !isClicked,
    "placeholder:text-transparent": isClicked,
  });

  const cancel = () => {
    setIsClicked(false);
    setText("");
  };

  const submit = async () => {
    try {
      await createComment(token!, text, threadInfo.id, null);
    } catch (e) {
      console.log(e);
    }

    cancel();
    refreshComments();
  };

  return (
    <div>
      <p></p>
      <Textarea
        value={text}
        className={textAreaStyle}
        onClick={() => setIsClicked(true)}
        onChange={(e) => setText(e.target.value)}
      />
      {isClicked && (
        <div className="flex flex-row justify-end w-[80%] gap-[15px] mt-[10px]">
          <button onClick={cancel}>Cancel</button>
          <button onClick={submit}>Comment</button>
        </div>
      )}
    </div>
  );
}

export default CommentCreate;
