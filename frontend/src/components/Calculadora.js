import React, { useState } from "react";
import { View, Button, TextInput } from "react";
import styles from "../css/styles";

export function Calculator() {
  const [input, setInput] = useState("");

  const handlePress = (value) => {
    setInput((prev) => prev + value);
  };

  const calculateResult = () => {
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
        <Button title="0" onPress={() => handlePress("0")} />
        <Button title="=" onPress={calculateResult} color="blue" />
        <Button title="C" onPress={() => setInput("")} color="red" />
      </View>
    </View>
  );
}