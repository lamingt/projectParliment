import axios from "axios";
import { useRevalidator } from "react-router";

const BACKEND_URL = `http://localhost:${import.meta.env.VITE_SERVER_PORT}`;

export const authRegister = async (email: string, username: string, password: string) => {
  const res = await axios.post(`${BACKEND_URL}/api/v1/user/register`, {
    email,
    username,
    password,
  });
  return res.data;
};

export const authLogin = async (email: string, password: string) => {
  const res = await axios.post(`${BACKEND_URL}/api/v1/user/login`, {
    email,
    password,
  });
  return res.data;
};

export const getThreadList = async (pageNum: string) => {
  const res = await axios.get(`${BACKEND_URL}/api/v1/threads/list?pageNum=${pageNum}`);
  return res.data;
};

export const createComment = async (
  token: string,
  text: string,
  threadId: string,
  parentComment: string | null
) => {
  const res = await axios.post(
    `${BACKEND_URL}/api/v1/comments`,
    {
      threadId,
      text,
      parentComment,
    },
    {
      headers: {
        Authorization: token,
      },
    }
  );

  return res.data;
};

export const getRootComments = async (threadId: string, pageNum: number, sort: string) => {
  const res = await axios.get(
    `${BACKEND_URL}/api/v1/comments/root?threadId=${threadId}&pageNum=${pageNum}&sort=${sort}`
  );

  return res.data.data;
};

export const getAvatar = async (userId: String) => {
  const res = await axios.get(`${BACKEND_URL}/api/v1/user/avatar/${userId}`);

  return res.data;
};
