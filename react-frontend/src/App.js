import React, { useEffect, useState } from 'react';
import './App.css';

function App() {
  const [text, setText] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/api/leaderboard')
    .then(response => response.text())
    .then(data => setText(data))
    .catch(error => console.error('Error:', error));
    } , []);

  return (
    <div className="App">
      <header className="App-header">
        <p>
          Current Leaderboard
          {text}
        </p>
      </header>
    </div>
  );
}

export default App;
