import { useState, useEffect } from "react";
import type { VaccinationType } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

type VaccinationTypeFormData = {
  vaccineName: string;
  description: string;
  durationMonths: number;
  requiredForAdoption: boolean;
};

type VaccinationTypeFormModalProps = {
  vaccinationType: VaccinationType | null;
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: VaccinationTypeFormData) => Promise<void>;
};

export default function VaccinationTypeFormModal({ vaccinationType, isOpen, onClose, onSubmit }: VaccinationTypeFormModalProps) {
  const [formData, setFormData] = useState<VaccinationTypeFormData>({
    vaccineName: "",
    description: "",
    durationMonths: 0,
    requiredForAdoption: false,
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (vaccinationType) {
      setFormData({
        vaccineName: vaccinationType.vaccineName,
        description: vaccinationType.description,
        durationMonths: vaccinationType.durationMonths,
        requiredForAdoption: vaccinationType.requiredForAdoption,
      });
    } else {
      setFormData({
        vaccineName: "",
        description: "",
        durationMonths: 0,
        requiredForAdoption: false,
      });
    }
  }, [vaccinationType]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target;
    const checked = (e.target as HTMLInputElement).checked;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : type === "number" ? Number(value) : value,
    }));
  };

  const handleSubmit = async () => {
    setIsSubmitting(true);
    setError(null);
    try {
      await onSubmit(formData);
      onClose();
    } catch (err) {
      setError("Failed to submit form. Please try again.");
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-lg w-[95vw] sm:w-full">
        <DialogHeader>
          <DialogTitle className="text-lg md:text-xl">{vaccinationType ? "Edit Vaccination Type" : "Add Vaccination Type"}</DialogTitle>
          <DialogDescription className="text-sm">
            {vaccinationType ? "Update the details of the vaccination type." : "Fill in the details for the new vaccination type."}
          </DialogDescription>
        </DialogHeader>
        <div className="space-y-4 mt-4">
          <div>
            <Label htmlFor="vaccineName">Vaccine Name</Label>
            <Input id="vaccineName" name="vaccineName" value={formData.vaccineName} onChange={handleChange} required />
          </div>
          <div>
            <Label htmlFor="description">Description</Label>
            <Input id="description" name="description" value={formData.description} onChange={handleChange} />
          </div>
          <div>
            <Label htmlFor="durationMonths">Duration (Months)</Label>
            <Input id="durationMonths" name="durationMonths" type="number" value={formData.durationMonths} onChange={handleChange} required />
          </div>
          <div className="flex items-center space-x-2">
            <input
              id="requiredForAdoption"
              name="requiredForAdoption"
              type="checkbox"
              checked={formData.requiredForAdoption}
              onChange={handleChange}
            />
            <Label htmlFor="requiredForAdoption">Required for Adoption</Label>
          </div>
          {error && <p className="text-red-500">{error}</p>}
        </div>
        <DialogFooter className="mt-4">
          <Button variant="secondary" onClick={onClose} disabled={isSubmitting}>
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={isSubmitting}>
            {isSubmitting ? "Submitting..." : "Submit"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
