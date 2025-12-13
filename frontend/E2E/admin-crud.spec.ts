import { test, expect } from "@playwright/test";

const ADMIN_EMAIL = process.env.ADMIN_EMAIL;
const ADMIN_PASSWORD = process.env.ADMIN_PASSWORD;

if (!ADMIN_EMAIL || !ADMIN_PASSWORD) {
  throw new Error("ADMIN_EMAIL and ADMIN_PASSWORD environment variables must be set");
}

test.beforeEach(async ({ page }) => {
  await page.goto("/login");

  await page.getByRole("textbox", { name: "Email" }).fill(ADMIN_EMAIL);
  await page.getByRole("textbox", { name: "Password" }).fill(ADMIN_PASSWORD);
  await page.getByRole("button", { name: "Login" }).click();

  // Wait for navigation away from login page
  await page.waitForURL((url) => !url.href.includes("/login"), { timeout: 10000 });

  // Navigate to admin and wait for the page to load
  await page.goto("/admin");
  await expect(page.getByRole("heading", { name: "Admin Dashboard" })).toBeVisible();
});

test("should display admin dashboard", async ({ page }) => {
  await expect(page.getByRole("heading", { name: "Admin Dashboard" })).toBeVisible();
});

test("should navigate to animal management", async ({ page }) => {
  await page.goto("/admin/animals");
  await expect(page.getByRole("heading", { name: "Animal Management" })).toBeVisible();
});

test("should create, edit, and delete an animal", async ({ page }) => {
  await page.goto("/admin/animals");

  // CREATE: Add a new animal
  await page.getByRole("button", { name: "Add Animal" }).click();
  await expect(page.getByRole("heading", { name: "Add New Animal" })).toBeVisible();

  const randomString = Math.random()
    .toString(36)
    .substring(2, 8)
    .replace(/[^a-z]/g, "a");
  const animalName = `AAAAATestPet${randomString}`;
  const updatedName = `AAAAAAUpdatedPet${randomString}`;
  const birthDate = new Date();
  birthDate.setFullYear(birthDate.getFullYear() - 2);
  const intakeDate = new Date();

  await page.getByLabel("Name").fill(animalName);
  await page.getByLabel("Sex").selectOption("female");
  await page.getByLabel("Birth Date").fill(birthDate.toISOString().split("T")[0]);
  await page.getByLabel("Intake Date").fill(intakeDate.toISOString().split("T")[0]);
  await page.getByLabel("Status").selectOption("AVAILABLE");
  await page.getByLabel("Adoption Fee ($)").fill("250");
  await page.getByLabel("Image URL").fill("https://example.com/dog.jpg");
  await page.getByLabel("Species").selectOption({ label: "Dog" });
  await page.getByLabel("Breed").waitFor({ state: "visible" });
  await page.getByLabel("Breed").selectOption({ index: 1 });

  await page.getByRole("button", { name: "Create Animal" }).click();
  await expect(page.getByRole("heading", { name: "Add New Animal" })).not.toBeVisible();
  await expect(page.getByText(animalName)).toBeVisible();

  // EDIT: Edit the created animal
  const animalRow = page.locator("table tbody tr", { has: page.getByText(animalName, { exact: true }) });
  await animalRow.getByRole("button").first().click();
  await expect(page.getByRole("heading", { name: "Edit Animal" })).toBeVisible();

  await page.getByLabel("Name").clear();
  await page.getByLabel("Name").fill(updatedName);
  await page.getByLabel("Adoption Fee ($)").clear();
  await page.getByLabel("Adoption Fee ($)").fill("350");

  await page.getByRole("button", { name: /update animal/i }).click();
  await expect(page.getByText(updatedName)).toBeVisible();
  await expect(page.getByText(animalName, { exact: true })).not.toBeVisible();

  // DELETE: Delete the edited animal
  const updatedAnimalRow = page.locator("table tbody tr", { has: page.getByText(updatedName, { exact: true }) });
  await updatedAnimalRow.getByRole("button").nth(1).click();

  await expect(page.getByText(/are you sure/i)).toBeVisible();
  await page.getByRole("button", { name: /delete|confirm/i }).click();

  await expect(page.locator("table tbody").getByText(updatedName, { exact: true })).not.toBeVisible();
});

test("should display animal count", async ({ page }) => {
  await page.goto("/admin/animals");
  await page.waitForSelector("table tbody tr");

  const rowCount = await page.locator("table tbody tr").count();

  await expect(page.getByText(`Showing ${rowCount} ${rowCount === 1 ? "result" : "results"}`)).toBeVisible();
});
