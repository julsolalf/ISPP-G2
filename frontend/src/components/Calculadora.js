import React, { useState } from "react";
import { View, Button, TextInput } from "react";
import styles from "../css/TPV/styles";

const Numpad = () => {
  const [input, setInput] = useState();

  const handlePress = (value) => {
    setInput((prev) => prev + value);
  };

  const s = () => {
    try {
      setInput(eval(input).toString());
    } catch (error) {
      setInput("Error");
    }
  };

  return (
    <View style={styles.card}>
      <TextInput style={styles.input} value={input} editable={false} />
      <View style={styles.grid}>
        {[...Array(9)].map((_, i) => (
          <Button key={i} title={(i + 1).toString()} onPress={() => handlePress((i + 1).toString())} />
        ))}
      </View>
    </View>
  );
};

export default Numpad;