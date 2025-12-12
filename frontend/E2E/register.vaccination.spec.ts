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
  


  test("Should navigate to Register Vaccination Overview page", async ({ page }) => {
    await page.getByRole("link", { name: "Medical Records" }).click();
    await expect(page).toHaveURL("/veterinarian/overview");
    await expect(page.getByText("Medical Record Overview")).toBeVisible();
    await page.getByRole("button", { name: "Vaccination Overview"}).click();
    await expect(page).toHaveURL("/veterinarian/vaccinations");
  });


  test("Should register a new vaccination", async ({ page }) => {
    await page.getByRole("link", { name: "Medical Records" }).click();
    await expect(page).toHaveURL("/veterinarian/overview");
    await expect(page.getByText("Medical Record Overview")).toBeVisible();
    await page.getByRole("button", { name: "Vaccination Overview"}).click();

    await expect(page).toHaveURL("/veterinarian/vaccinations");
    await page.getByRole("button", { name: "Add Vaccination" }).click();

    await expect(page).toHaveURL("/veterinarian/vaccinations");

    await page.getByLabel("Animal").selectOption("7");
    await page.getByLabel("Vaccination Type").selectOption("1");

const administered = "2025-12-12";
await page.locator("#dateAdministered").fill(administered);

await page.getByLabel("Vaccination Type").selectOption("1");

function addYears(iso: string, years: number) {
  const d = new Date(iso);
  d.setFullYear(d.getFullYear() + years);
  return d.toISOString().split("T")[0];
}

const expectedNext = addYears(administered, 1);

await expect.poll(() => page.locator("#nextDueDate").inputValue(), { timeout: 2000 }).toBe(expectedNext);


await page.getByRole("button", { name: "Add Vaccination" }).click();
await expect(page).toHaveURL("/veterinarian/vaccinations");
});
