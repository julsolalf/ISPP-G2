import WebSocket from 'ws'
globalThis.WebSocket = WebSocket as any;
import { test } from 'magnitude-test';

// Learn more about building test case:
// https://docs.magnitude.run/core-concepts/building-test-cases

const sampleTodos = [
    "Take out the trash",
    "Buy groceries",
    "Build more test cases with Magnitude"
];

test('Registrate e inicia sesion', { url: 'http://localhost:3000' })
    .step('Register an user on the "Registrarse" button, and create and user with fake data, note that the phone number needs to have 9 digits and password must be longer than 8 characters plus have Mayus, minus, number and a special character')
    .check('should redirect to "elegirNegocio" page after a few Seconds and you should see a "registrar negocio" buttom')

