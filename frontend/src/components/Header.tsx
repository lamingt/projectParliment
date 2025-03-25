import { Link } from "react-router";
import logo from "../assets/react.svg";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

function Header() {
  console.log(AuthContext);
  const { token } = useContext(AuthContext);

  return (
    <header className="min-h-[4vh] border-b border-gray-500 flex items-center">
      <div className="container mx-auto flex flex-row items-center justify-between">
        <Link to="/">
          <img src={logo} className="ml-0"></img>
        </Link>

        <input className="bg-gray-200" placeholder="Search" />

        <nav className="flex gap-5">
          {token ? (
            <div>Edit Profile</div>
          ) : (
            <>
              <Link to="/register">Register</Link>
              <Link to="/login">Login</Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}

export default Header;
