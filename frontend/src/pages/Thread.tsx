import { useLocation } from "react-router";

type threadInfoType = {
  id: string;
  title: string;
  summary: string;
  date: string;
  chamber: string;
  active: boolean;
  numLikes: number;
  numComments: number;
};

function Thread() {
  const location = useLocation();
  const threadInfo: threadInfoType = location.state;
  return <div>{JSON.stringify(threadInfo)}</div>;
}

export default Thread;
