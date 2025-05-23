import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { Link, Navigate } from "react-router";
import { SubmitHandler, useForm } from "react-hook-form";
import { authRegister } from "../api";

type FormFields = {
  email: string;
  username: string;
  password: string;
  confirmPassword: string;
};

function Register() {
  const { token, setToken } = useContext(AuthContext);
  const {
    handleSubmit,
    register,
    setError,
    watch,
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
          placeholder="Email"
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
          placeholder="Username"
        />
        {errors.username && <p className="text-red-600">{errors.username.message}</p>}
        <input
          {...register("password", {
            required: "Password is required",
            minLength: {
              value: 5,
              message: "Password must be at least 5 characters",
            },
            pattern: {
              value: /^.*[a-zA-Z0-9].*$/,
              message: "Password must contain at least one alphanumeric character",
            },
          })}
          type="password"
          placeholder="Password"
        />
        <input
          {...register("confirmPassword", {
            required: "Confirm password",
            validate: (value: string) => {
              if (watch("password") != value) {
                return "Passwords do not match";
              }
            },
          })}
          type="password"
          placeholder="Confirm Password"
        />
        {errors.confirmPassword && <p className="text-red-600">{errors.confirmPassword.message}</p>}
        <button disabled={isSubmitting} type="submit">
          Register
        </button>
        {isSubmitting && (
          <div role="status" className="h-screen flex items-center justify-center">
            <svg
              aria-hidden="true"
              className="w-10 h-10 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600"
              viewBox="0 0 100 101"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                fill="currentColor"
              />
              <path
                d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                fill="currentFill"
              />
            </svg>
            <span className="sr-only">Loading...</span>
          </div>
        )}
        {errors.root && <p className="text-red-600">{errors.root.message}</p>}
      </form>
      <p>
        Have an account? <Link to="/login">Login</Link>
      </p>
    </div>
  );
}

export default Register;
