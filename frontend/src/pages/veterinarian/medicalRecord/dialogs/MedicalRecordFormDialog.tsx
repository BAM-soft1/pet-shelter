import { useState, useEffect } from "react";
import type { MedicalRecord, Animal } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { DatePicker } from "@/components/ui/datepicker";
import { AnimalService } from "@/api/animals";

type MedicalRecordFormData = {
  animalId: number | null;
  date: string;
  diagnosis: string;
  treatment: string;
  cost: number;
};

type MedicalRecordFormDialogProps = {
  record: MedicalRecord | null;
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: MedicalRecordFormData) => Promise<void>;
};

export default function MedicalRecordFormDialog({ record, isOpen, onClose, onSubmit }: MedicalRecordFormDialogProps) {
  const [formData, setFormData] = useState<MedicalRecordFormData>({
    animalId: null,
    date: "",
    diagnosis: "",
    treatment: "",
    cost: 0,
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [animals, setAnimals] = useState<Animal[]>([]);

  useEffect(() => {
    if (record) {
      setFormData({
        animalId: record.animal.id,
        date: new Date(record.date).toISOString().split("T")[0],
        diagnosis: record.diagnosis,
        treatment: record.treatment,
        cost: record.cost,
      });
    } else {
      setFormData({
        animalId: null,
        date: "",
        diagnosis: "",
        treatment: "",
        cost: 0,
      });
    }
  }, [record]);

  useEffect(() => {
    const fetchAnimals = async () => {
      try {
        const pageResponse = await AnimalService.getAnimals(
          0,
          1000,
          "name",
          "asc",
          undefined, // no status filter
          true, // only active animals
          false // animals WITHOUT required vaccinations
        );
        setAnimals(pageResponse.content);
      } catch (err) {
        console.error("Failed to fetch animals:", err);
      }
    };
    fetchAnimals();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "cost" || name === "animalId" ? Number(value) : value,
    }));
  };

  const handleDateChange = (date: Date | null) => {
    if (date) {
      setFormData((prev) => ({
        ...prev,
        date: date.toISOString().split("T")[0],
      }));
    }
  };

  const handleSubmit = async () => {
    setIsSubmitting(true);
    setError(null);
    try {
      if (formData.animalId === null) {
        setError("Please select an animal.");
        setIsSubmitting(false);
        return;
      }
      await onSubmit(formData);
      onClose();
    } catch (err) {
      setError("Failed to submit the form. Please try again.");
      console.log(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const selectedAnimal = animals.find((a) => a.id === formData.animalId);

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle className="text-2xl">{record ? "Edit Medical Record" : "New Medical Record"}</DialogTitle>
          <DialogDescription>
            {record ? "Update the details of the medical record." : "Fill in the details for a new medical record."}
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-5 py-4">
          <div className="space-y-2">
            <Label htmlFor="animalId">Patient</Label>
            <div className="flex gap-3 items-center">
              {selectedAnimal?.imageUrl && (
                <div className="w-12 h-12 rounded-lg overflow-hidden border-2 border-indigo-200 shrink-0">
                  <img src={selectedAnimal.imageUrl} alt={selectedAnimal.name} className="w-full h-full object-cover" />
                </div>
              )}
              <select
                id="animalId"
                name="animalId"
                value={formData.animalId ?? ""}
                onChange={handleChange}
                className="flex-1 h-10 px-3 rounded-md border border-input bg-background text-sm ring-offset-background focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2"
              >
                <option value="" disabled>
                  Select an animal
                </option>
                {animals.map((animal) => (
                  <option key={animal.id} value={animal.id}>
                    {animal.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="date">Date</Label>
            <DatePicker id="date" selectedDate={formData.date ? new Date(formData.date) : null} onDateChange={handleDateChange} className="w-full" />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="diagnosis">Diagnosis</Label>
              <Textarea
                id="diagnosis"
                name="diagnosis"
                value={formData.diagnosis}
                onChange={handleChange}
                placeholder="Enter diagnosis..."
                className="min-h-[100px] resize-none"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="treatment">Treatment</Label>
              <Textarea
                id="treatment"
                name="treatment"
                value={formData.treatment}
                onChange={handleChange}
                placeholder="Enter treatment..."
                className="min-h-[100px] resize-none"
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="cost">Medical Cost: ($)</Label>
            <Input type="number" id="cost" name="cost" value={formData.cost} onChange={handleChange} min={0} step={0.01} />
          </div>

          {error && <div className="p-3 rounded-md bg-red-50 border border-red-200 text-red-700 text-sm">{error}</div>}
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={onClose} disabled={isSubmitting}>
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={isSubmitting}>
            {isSubmitting ? "Saving..." : record ? "Update" : "Create"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
