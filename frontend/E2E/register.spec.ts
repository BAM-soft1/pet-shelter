import { test, expect } from "@playwright/test";

test.beforeEach(async ({ page }) => {
  await page.goto("http://localhost/register");
});

test("should register successfully with valid data", async ({ page }) => {
  const timestamp = Date.now();
  const email = `newuser${timestamp}@mail.com`;

  await page.getByLabel("First Name").fill("New");
  await page.getByLabel("Last Name").fill("User");
  await page.getByLabel("Email").fill(email);
  await page.getByLabel("Phone (Optional)").fill("+4512345678");
  await page.getByLabel("Password", { exact: true }).fill("Test1234!");
  await page.getByLabel("Confirm Password").fill("Test1234!");

  await page.getByRole("button", { name: "Create Account" }).click();

  await expect(page).toHaveURL("http://localhost/");
});

test("should fail when registering with existing email", async ({ page }) => {
  const existingEmail = "testuser@mail.com";

  await page.getByLabel("First Name").fill("Test");
  await page.getByLabel("Last Name").fill("User");
  await page.getByLabel("Email").fill(existingEmail);
  await page.getByLabel("Password", { exact: true }).fill("Test1234!");
  await page.getByLabel("Confirm Password").fill("Test1234!");

  await page.getByRole("button", { name: "Create Account" }).click();

  await expect(page.getByText("Email already in use")).toBeVisible();
});

test("should show validation error for password mismatch", async ({ page }) => {
  await page.getByLabel("First Name").fill("Test");
  await page.getByLabel("Last Name").fill("User");
  await page.getByLabel("Email").fill("mismatch@test.com");
  await page.getByLabel("Password", { exact: true }).fill("Test1234!");
  await page.getByLabel("Confirm Password").fill("Different123!");

  await page.getByRole("button", { name: "Create Account" }).click();

  await expect(page.getByText("Passwords do not match")).toBeVisible();
});

test("should show validation error for weak password", async ({ page }) => {
  await page.getByLabel("First Name").fill("Test");
  await page.getByLabel("Last Name").fill("User");
  await page.getByLabel("Email").fill("weak@test.com");
  await page.getByLabel("Password", { exact: true }).fill("weak");
  await page.getByLabel("Confirm Password").fill("weak");

  await page.getByRole("button", { name: "Create Account" }).click();

  await expect(page.getByText("Password must be at least 7 characters")).toBeVisible();
});

test("should show validation error for invalid phone format", async ({ page }) => {
  await page.getByLabel("First Name").fill("Test");
  await page.getByLabel("Last Name").fill("User");
  await page.getByLabel("Email").fill("phone@test.com");
  await page.getByLabel("Phone (Optional)").fill("12345678");
  await page.getByLabel("Password", { exact: true }).fill("Test1234!");
  await page.getByLabel("Confirm Password").fill("Test1234!");

  await page.getByRole("button", { name: "Create Account" }).click();

  await expect(page.getByText("Invalid phone format. Must be +45 followed by 8 digits")).toBeVisible();
});
