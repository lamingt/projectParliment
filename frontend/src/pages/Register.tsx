import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { Navigate } from "react-router";
import { SubmitHandler, useForm } from "react-hook-form";
import { authRegister } from "../api";

type FormFields = {
  email: string;
  username: string;
  password: string;
};

function Register() {
  const { token, setToken } = useContext(AuthContext);
  const {
    handleSubmit,
    register,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<FormFields>();
  if (token) {
    return <Navigate to="/" />;
  }

  const onSubmit: SubmitHandler<FormFields> = async (data) => {
    try {
      const res = await authRegister(data.email, data.username, data.password);
      console.log(res);
      setToken(res.data.token);
    } catch (error: any) {
      if (error?.response?.data?.message) {
        const errorMessage: string = error.response.data.message.toLowerCase();

        if (errorMessage.includes("email")) {
          setError("email", {
            type: "manual",
            message: errorMessage,
          });
        } else if (errorMessage.includes("Username")) {
          setError("username", {
            type: "manual",
            message: errorMessage,
          });
        } else if (errorMessage.includes("Password")) {
          setError("password", {
            type: "manual",
            message: errorMessage,
          });
        } else {
          setError("root", {
            type: "manual",
            message: errorMessage || "An unexpected error occurred. Please try again.",
          });
        }
      } else {
        setError("root", {
          type: "manual",
          message: "An unexpected error occurred. Please try again.",
        });
      }
    }
  };

  return (
    <div className="h-screen flex flex-col items-center justify-center">
      <form onSubmit={handleSubmit(onSubmit)}>
        <input
          {...register("email", {
            required: "Email is required",
            pattern: {
              value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
              message: "Invalid email address",
            },
          })}
          type="text"
          placeholder="email"
        />
        {errors.email && <p className="text-red-600">{errors.email.message}</p>}
        <input
          {...register("username", {
            required: "Username is required",
            minLength: {
              value: 3,
              message: "Username must be at least 3 characters",
            },
            maxLength: {
              value: 20,
              message: "Username length cannot exceed 20 characters",
            },
          })}
          type="text"
          placeholder="username"
        />
        {errors.username && <p className="text-red-600">{errors.username.message}</p>}
        <input
          {...register("password", {
            required: true,
            minLength: {
              value: 8,
              message: "Password must be at least 8 characters",
            },
          })}
          type="password"
          placeholder="password"
        />
        {errors.password && <p className="text-red-600">{errors.password.message}</p>}
        <button disabled={isSubmitting} type="submit">
          {isSubmitting ? "Registering.." : "Register"}
        </button>
        {errors.root && <p className="text-red-600">{errors.root.message}</p>}
      </form>
    </div>
  );
}

export default Register;
