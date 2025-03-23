import React from 'react';

const Numpad = () => {
    return (
      <div className="numpad">
        <button>1</button>
        <button>2</button>
        <button>3</button>
        <button>4</button>
        <button>5</button>
        <button>6</button>
        <button>7</button>
        <button>8</button>
        <button>9</button>
        <button className="zero-button">0</button> {/* Centered "0" button */}
      </div>
    );
  };
export default Numpad;