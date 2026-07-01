import { useNavigate } from "react-router";

function WelcomePage(){
  const navigate = useNavigate();

  function handleContinue() {
    navigate("/dashboard");
  }
  return (
    <main className="welcome-page">
      <div className="background-glow background-glow-one"/>
      <div className="background-glow background-glow-two"/>

      <section className="welcome-panel">
        <p className="welcome-logo">Wallex</p>

        <div className="welcome-content">
          <p className="welcome-label">Welcome</p>
          <h1 className="welcome-heading">
            A clearer view of your money.
          </h1>
          <p className="welcome-description">
            Track transactions, import statements, and understand your
            spending in one place.
          </p>
        </div>
        <button 
        className="continue-button"
        type= "button"
        onClick={handleContinue}>
          Continue
          <span aria-hidden ="true"> → </span>
        </button>
      </section>
    </main>
  );
}
export default WelcomePage;
