import { CardContent } from "./ui/card";
import { type Animal, type AdoptionApplicationRequest, type AuthUser } from "@/types/types";
import { Button } from "./ui/button";
import { Label } from "./ui/label";
import { Input } from "./ui/input";
import { Textarea } from "./ui/textarea";
import { Spinner } from "././ui/spinner";
import { useState } from "react";
import { AdoptionApplicationService } from "@/api/adoptionApplication";
import { useNavigate, useLocation } from "react-router-dom";

interface AdoptApplicationFormProps {
  animal: Animal;
  user: AuthUser | null;
  onSubmit?: (application: AdoptionApplicationRequest) => void;
}

export default function AdoptApplicationForm({ animal, user, onSubmit }: AdoptApplicationFormProps) {
  const [description, setDescription] = useState("");
  const [submitted, setSubmitted] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();
  const location = useLocation();

  const handleLoginRedirect = () => {
    // Send the user to login and return here after login
    navigate("/login", { state: { from: location, animal } });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return handleLoginRedirect();

    const application: AdoptionApplicationRequest = {
      userId: user.id,
      animalId: animal.id,
      description,
    };

    setSubmitting(true);
    setError(null);

    // Start timer and API call simultaneously
    const minLoadingTime = new Promise((resolve) => setTimeout(resolve, 1000));

    try {
      // Wait for both the API call and minimum loading time
      const [created] = await Promise.all([AdoptionApplicationService.createAdoptionApplication(application), minLoadingTime]);

      if (onSubmit) onSubmit(created);
      setSubmitted(true);
    } catch (err) {
      console.error("Failed to create adoption application", err);
      setError("Failed to submit application. Please try again.");
      // Still wait for minimum time even on error for consistent UX
      await minLoadingTime;
    } finally {
      setSubmitting(false);
    }
  };

  if (!user) {
    return (
      <CardContent>
        <div className="p-4">
          <p className="mb-4">You need to be logged in to apply for adoption.</p>
          <div className="flex gap-2">
            <Button onClick={handleLoginRedirect}>Login</Button>
            <Button variant="outline" onClick={() => navigate(-1)}>
              Back
            </Button>
          </div>
        </div>
      </CardContent>
    );
  }

  return (
    <CardContent>
      {submitted ? (
        <div className="p-4 rounded-md bg-green-50 text-green-800">Thanks — your application was submitted.</div>
      ) : (
        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="flex flex-col gap-1">
            <Label>Name</Label>
            <Input value={`${user.firstName} ${user.lastName}`} readOnly />
          </div>

          <div className="flex flex-col gap-1">
            <Label>Email</Label>
            <Input value={user.email} readOnly />
          </div>

          <div className="flex flex-col gap-1">
            <Label>Phone</Label>
            <Input value={user.phone ?? ""} readOnly />
          </div>

          <div className="flex flex-col gap-1">
            <Label>Message</Label>
            <Textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder={`Why would you like to adopt ${animal.name}?`}
            />
          </div>

          <div className="md:col-span-2 flex flex-col gap-2 mt-2">
            {error && <div className="text-destructive text-sm">{error}</div>}
            <div className="flex gap-2">
              <Button type="submit" disabled={submitting}>
                {submitting ? (
                  <span className="flex items-center gap-2">
                    <Spinner />
                    Submitting...
                  </span>
                ) : (
                  "Submit Application"
                )}
              </Button>
              <Button variant="outline" onClick={() => setDescription("")}>
                Reset
              </Button>
            </div>
          </div>
        </form>
      )}
    </CardContent>
  );
}
