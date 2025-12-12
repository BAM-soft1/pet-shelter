import { test, expect } from "@playwright/test";


const VET_EMAIL = process.env.VET_EMAIL;
const VET_PASSWORD = process.env.VET_PASSWORD;

if (!VET_EMAIL || !VET_PASSWORD) {
  throw new Error("VET_EMAIL and VET_PASSWORD must be set in environment variables");
}

test.beforeEach((async ({ page }) => {
  await page.goto("/login");


  await page.getByLabel("Email").fill(VET_EMAIL);
  await page.getByLabel("Password").fill(VET_PASSWORD);
  await page.getByRole("button", { name: "Login" }).click();

  await page.waitForURL((url) => !url.href.includes("/login"), { timeout: 10000 });


  await expect(page).toHaveURL("/");
  await expect(page.getByText("Every Pet Deserves a Loving Home")).toBeVisible();
}));

test("Should Display home page after login", async ({ page }) => {
  await expect(page).toHaveURL("/");
  await expect(page.getByText("Every Pet Deserves a Loving Home")).toBeVisible();
});

test("Should navigate to Register Medical Record page", async ({ page }) => {
  await page.getByRole("link", { name: "Medical Records" }).click();
  await expect(page).toHaveURL("/veterinarian/overview");
  await expect(page.getByText("Medical Record Overview")).toBeVisible();
});

test("Should register a new medical record", async ({ page }) => {
  await page.getByRole("link", { name: "Medical Records" }).click();
  await expect(page).toHaveURL("/veterinarian/overview");

  await page.getByRole("button", { name: "Add Record" }).click();
  await expect(page).toHaveURL("/veterinarian/overview");

  await page.getByLabel("Patient").selectOption("5");
  await page.getByLabel("Date").click();
  await page.waitForSelector('[role="dialog"]', { timeout: 5000 });
  await page.getByRole("gridcell", { name: "13" }).click();


  await page.getByLabel(/diagnosis/i).fill("Routine checkup");
  await page.getByLabel(/treatment/i).fill("Vaccination");
  await page.getByLabel("Medical Cost").fill("150");

  await page.getByRole("button", { name: "Create" }).click();

  await expect(page.getByText("Medical Record Overview")).toBeVisible();
});

test("Should edit a medical record", async ({ page }) => {
  await page.getByRole("link", { name: "Medical Records" }).click();
  await expect(page).toHaveURL("/veterinarian/overview");

  await page.getByRole("button", { name: "Edit", exact: true }).first().click();

  await page.getByLabel(/diagnosis/i).fill("Updated diagnosis");
  await page.getByRole("button", { name: "Update" }).click();

  await expect(page.getByText("Medical Record Overview")).toBeVisible();
});

test("Should delete a medical record", async ({ page }) => {
  await page.getByRole("link", { name: "Medical Records" }).click();
  await expect(page).toHaveURL("/veterinarian/overview");

  await page.getByRole("button", { name: "Delete", exact: true }).first().click();

  await page.getByRole("button", { name: "Delete Record" }).click();

  await expect(page.getByText("Medical Record Overview")).toBeVisible();
});

