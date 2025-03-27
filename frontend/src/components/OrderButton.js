import React from "react";
import { Button, View } from "react";
import styles from "../css/styles";

export function OrderButton({ product, addOrder }) {
  return (
    <View style={styles.buttonContainer}>
      <Button title={`${product.name} - $${product.price}`} onPress={() => addOrder(product)} color="green" />
    </View>
  );
}
