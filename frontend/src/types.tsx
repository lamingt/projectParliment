export type ThreadInfoType = {
  id: string;
  title: string;
  summary: string;
  date: string;
  chamber: string;
  active: boolean;
  status: string;
  numLikes: number;
  numComments: number;
  likeStatus: string;
};

export type CommentsResponseType = {
  pagination: {
    numPages: number;
    currentPage: number;
    numItems: number;
  };
  commentInfo: [CommentsInfoType];
};

export type CommentsInfoType = {
  id: string;
  threadId: string;
  creatorInfo: {
    userId: string;
    username: string;
  };
  text: string;
  parentCommentId: string;
  netLikes: number;
  createdAt: string;
};
