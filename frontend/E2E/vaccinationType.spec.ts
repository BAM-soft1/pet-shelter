import { test, expect } from "@playwright/test";

const VET_EMAIL = process.env.VET_EMAIL;
const VET_PASSWORD = process.env.VET_PASSWORD;

if (!VET_EMAIL || !VET_PASSWORD) {
  throw new Error("VET_EMAIL and VET_PASSWORD must be set in environment variables");
}

test.beforeEach(async ({ page }) => {
  await page.goto("/login");

  await page.getByLabel("Email").fill(VET_EMAIL);
  await page.getByLabel("Password").fill(VET_PASSWORD);
  await page.getByRole("button", { name: "Login" }).click();

  await page.waitForURL((url) => !url.href.includes("/login"), { timeout: 10000 });

  await expect(page).toHaveURL("/");
  await expect(page.getByText("Every Pet Deserves a Loving Home")).toBeVisible();
});

test("Should Display home page after login", async ({ page }) => {
  await expect(page).toHaveURL("/");
  await expect(page.getByText("Every Pet Deserves a Loving Home")).toBeVisible();
});

test("Should navigate to Vaccination Type Overview page", async ({ page }) => {
  await page.getByRole("link", { name: "Admin" }).click();
  await expect(page).toHaveURL("admin/veterinarian/overview");
  await expect(page.getByText("Medical Record Overview")).toBeVisible();
  await page.getByRole("link", { name: "Vaccination Types" }).click();
  await expect(page).toHaveURL("admin/veterinarian/vaccinations-types");
});

test("Should create, edit, and delete a vaccination type", async ({ page }) => {
  await page.getByRole("link", { name: "Admin" }).click();
  await expect(page.getByText("Medical Record Overview")).toBeVisible();
  await page.getByRole("link", { name: "Vaccination Types" }).click();
  await expect(page).toHaveURL("admin/veterinarian/vaccinations-types");

  // CREATE: Add a new vaccination type
  await page.getByRole("button", { name: "Add Vaccination Type" }).click();

  const testName = `TestVaccine${Math.random().toString(36).substring(2, 8)}`;
  const updatedName = `Updated${testName}`;

  await page.getByLabel(/name/i).fill(testName);
  await page.getByLabel(/description/i).fill("This is a test vaccination type");
  await page.getByLabel(/duration/i).fill("12");
  await page.locator("#requiredForAdoption").check();

  await page.getByRole("button", { name: "Submit" }).click();
  await expect(page).toHaveURL("admin/veterinarian/vaccinations-types");
  await expect(page.getByText(testName)).toBeVisible();

  // EDIT: Edit the created vaccination type
  const typeRow = page.locator("table tbody tr", { has: page.getByText(testName, { exact: true }) });
  await typeRow.getByRole("button", { name: "Edit" }).click();

  await page.getByLabel(/name/i).clear();
  await page.getByLabel(/name/i).fill(updatedName);
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page.getByText(updatedName)).toBeVisible();
  await expect(page.getByText(testName, { exact: true })).not.toBeVisible();

  // DELETE: Delete the edited vaccination type
  const updatedTypeRow = page.locator("table tbody tr", { has: page.getByText(updatedName, { exact: true }) });
  await updatedTypeRow.getByRole("button", { name: "Delete" }).click();

  await page.getByRole("button", { name: "Delete" }).click();

  await expect(page.locator("table tbody").getByText(updatedName, { exact: true })).not.toBeVisible();
});
