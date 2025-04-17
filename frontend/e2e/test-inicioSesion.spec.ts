import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/');
  await page.getByRole('button', { name: 'Iniciar Sesión' }).click();
  await page.getByRole('textbox', { name: 'Usuario' }).click();
  await page.getByRole('textbox', { name: 'Usuario' }).fill('juan');
  await page.getByRole('textbox', { name: 'Usuario' }).press('Tab');
  await page.getByRole('textbox', { name: 'Contrasena' }).fill('password');
  await page.getByRole('button', { name: 'Iniciar Sesión' }).click();
  await page.locator('svg').nth(1).click();
  await page.getByRole('button', { name: 'Ver Perfil' }).click();
  await page.getByText('juan', { exact: true }).click();
  await page.getByRole('button', { name: 'Ver planes' }).click();

});