import { useState } from "react";
import { Textarea } from "./ui/textarea";
import clsx from "clsx";

function CommentCreate() {
  const [isClicked, setIsClicked] = useState(false);
  const [text, setText] = useState("");

  const textAreaStyle = clsx({
    "w-[80%]": true,
    "border-black": true,
    "h-[2vh]": !isClicked,
    "h-[10vh]": isClicked,
    "placeholder:text-lg": !isClicked,
    "placeholder:text-transparent": isClicked,
  });

  const cancel = () => {
    setIsClicked(false);
    setText("");
  };

  const submit = () => {
    console.log(text);

    cancel();
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
