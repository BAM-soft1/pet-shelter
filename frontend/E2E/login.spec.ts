import { test, expect } from "@playwright/test";

test("should login successfully with test account", async ({ page }) => {
  const testUsername = "testuser@mail.com";
  const testPassword = "Testpassword!";

  await page.goto("http://localhost/");
  await page.getByRole("link", { name: "Log in" }).click();

  // Wait for navigation to login page
  await expect(page).toHaveURL(/.*\/login/);

  await page.getByRole("textbox", { name: "Email" }).fill(testUsername);
  await page.getByRole("textbox", { name: "Password" }).fill(testPassword);
  await page.getByRole("button", { name: "Login" }).click();

  // Wait for redirection to home page
  await expect(page).toHaveURL("http://localhost/");
});

test("should show error message with invalid credentials", async ({ page }) => {
  const invalidUsername = "invaliduser@mail.com";
  const invalidPassword = "InvalidPassword!";

  await page.goto("http://localhost/");
  await page.getByRole("link", { name: "Log in" }).click();

  await page.getByRole("textbox", { name: "Email" }).fill(invalidUsername);
  await page.getByRole("textbox", { name: "Password" }).fill(invalidPassword);
  await page.getByRole("button", { name: "Login" }).click();

  await expect(page.getByText("Invalid email or password")).toBeVisible();
});
