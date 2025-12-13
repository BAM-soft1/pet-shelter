import { useState } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Field, FieldDescription, FieldGroup, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";

interface SignupFormProps {
  onSubmit: (data: {
    email: string
    firstName: string
    lastName: string
    phone?: string | null
    password: string
  }) => Promise<void>
  error?: string
  isLoading?: boolean
}

export function SignupForm({ onSubmit, error, isLoading = false }: SignupFormProps) {
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [validationError, setValidationError] = useState("");

  const validatePassword = (pwd: string): string | null => {
    if (pwd.length < 7) {
      return "Password must be at least 7 characters";
    }
    if (!/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(pwd)) {
      return "Password must include at least one special character";
    }
    return null;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setValidationError("");

    // Client-side validation
    const passwordError = validatePassword(password);
    if (passwordError) {
      setValidationError(passwordError);
      return;
    }

    if (password !== confirmPassword) {
      setValidationError("Passwords do not match");
      return;
    }

    if (firstName.trim().length < 2 || firstName.trim().length > 80) {
      setValidationError("First name must be between 2 and 80 characters");
      return;
    }

    if (lastName.trim().length < 2 || lastName.trim().length > 80) {
      setValidationError("Last name must be between 2 and 80 characters");
      return;
    }

    if (phone) {
      // International phone format: 7-32 characters, allowing numbers, +, -, (), and spaces
      if (!/^[0-9+\-() ]{7,32}$/.test(phone)) {
        setValidationError("Invalid phone format. Must be 7-32 characters (numbers, +, -, (), spaces allowed)");
        return;
      }
    }

    await onSubmit({
      email,
      firstName: firstName.trim(),
      lastName: lastName.trim(),
      phone: phone.trim() || null,
      password,
    });
  };

  const displayError = validationError || error;

  return (
    <Card>
      <CardHeader>
        <CardTitle>Create an account</CardTitle>
        <CardDescription>Enter your information below to create your account</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit}>
          <FieldGroup>
            {displayError && <div className="rounded-md bg-destructive/15 p-3 text-sm text-destructive">{displayError}</div>}
            <div className="grid grid-cols-2 gap-4">
              <Field>
                <FieldLabel htmlFor="firstName">First Name</FieldLabel>
                <Input
                  id="firstName"
                  type="text"
                  placeholder="John"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
                  disabled={isLoading}
                  required
                />
              </Field>
              <Field>
                <FieldLabel htmlFor="lastName">Last Name</FieldLabel>
                <Input
                  id="lastName"
                  type="text"
                  placeholder="Doe"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                  disabled={isLoading}
                  required
                />
              </Field>
            </div>
            <Field>
              <FieldLabel htmlFor="email">Email</FieldLabel>
              <Input
                id="email"
                type="email"
                placeholder="m@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                disabled={isLoading}
                required
              />
              <FieldDescription>We&apos;ll use this to contact you. We will not share your email with anyone else.</FieldDescription>
            </Field>
            <Field>
              <FieldLabel htmlFor="phone">Phone (Optional)</FieldLabel>
              <Input
                id="phone"
                type="tel"
                placeholder="+45 12 34 56 78"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                disabled={isLoading}
              />
            </Field>
            <Field>
              <FieldLabel htmlFor="password">Password</FieldLabel>
              <Input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} disabled={isLoading} required />
              <FieldDescription>Must be at least 7 characters with one special character.</FieldDescription>
            </Field>
            <Field>
              <FieldLabel htmlFor="confirm-password">Confirm Password</FieldLabel>
              <Input
                id="confirm-password"
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                disabled={isLoading}
                required
              />
              <FieldDescription>Please confirm your password.</FieldDescription>
            </Field>
            <Field>
              <Button type="submit" disabled={isLoading} className="w-full">
                {isLoading ? "Creating Account..." : "Create Account"}
              </Button>
              <FieldDescription className="text-center">
                Already have an account?{" "}
                <Link to="/login" className="underline underline-offset-4 hover:text-primary">
                  Sign in
                </Link>
              </FieldDescription>
            </Field>
          </FieldGroup>
        </form>
      </CardContent>
    </Card>
  );
}
