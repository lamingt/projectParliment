import axios from "axios";

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
