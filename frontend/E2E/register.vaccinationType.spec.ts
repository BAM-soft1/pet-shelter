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
  


  test("Should navigate to Register Vaccination Type Overview page", async ({ page }) => {
    await page.getByRole("link", { name: "Medical Records" }).click();
    await expect(page).toHaveURL("/veterinarian/overview");
    await expect(page.getByText("Medical Record Overview")).toBeVisible();
    await page.getByRole("link", { name: "Vaccination Types" }).click();
    await expect(page).toHaveURL("/veterinarian/vaccinations-types");
  });


  test("Should register a new vaccination type", async ({ page }) => {
    await page.getByRole("link", { name: "Medical Records" }).click();
    await expect(page.getByText("Medical Record Overview")).toBeVisible();
    await page.getByRole("link", { name: "Vaccination Types" }).click();
    await page.getByRole("button", { name: "Add Vaccination Type" }).click();
    await expect(page).toHaveURL("/veterinarian/vaccinations-types");

    await page.getByLabel(/name/i).fill("test");
    await page.getByLabel(/description/i).fill("This is a test vaccination type");

    await page.getByLabel(/duration/i).fill("12");


    await page.locator("#requiredForAdoption").check();

    await page.getByRole("button", { name: "Submit" }).click();

    await expect(page).toHaveURL("/veterinarian/vaccinations-types");


  });

  test("Should edit a vaccination type", async ({ page }) => {
    await page.getByRole("link", { name: "Medical Records" }).click();
    await expect(page).toHaveURL("/veterinarian/overview");
    await expect(page.getByText("Medical Record Overview")).toBeVisible();
    await page.getByRole("link", { name: "Vaccination Types" }).click();

    await expect(page).toHaveURL("/veterinarian/vaccinations-types");

    await page.getByRole("button", { name: "Edit" }).first().click();

    await expect(page).toHaveURL("/veterinarian/vaccinations-types");

    await page.getByLabel(/name/i).fill("updated test");
    await page.getByRole("button", { name: "Submit" }).click();

    await expect(page).toHaveURL("/veterinarian/vaccinations-types");
  });

  test("Should delete a vaccination type", async ({page}) => {
    await page.getByRole("link", { name: "Medical Records" }).click();
    await expect(page).toHaveURL("/veterinarian/overview");
    await expect(page.getByText("Medical Record Overview")).toBeVisible();
    await page.getByRole("link", { name: "Vaccination Types" }).click();
  
    await expect(page).toHaveURL("/veterinarian/vaccinations-types");
  
    await page.getByRole("button", { name: "Delete" }).first().click();
    
    await page.getByRole("button", { name: "Delete" }).click();
  
    await expect(page).toHaveURL("/veterinarian/vaccinations-types");
  });
  

