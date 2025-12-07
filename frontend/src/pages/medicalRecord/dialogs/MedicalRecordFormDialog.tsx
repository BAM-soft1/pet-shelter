import { useState, useEffect } from "react";
import type { MedicalRecord, Animal } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { DatePicker } from "@/components/ui/datepicker";
import { AnimalService } from "@/api/animals";
import { useAuth } from "@/context/AuthProvider";

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
    const auth = useAuth();
    
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
                const animalsData = await AnimalService.getAnimals();
                setAnimals(animalsData);
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
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>{record ? "Edit Medical Record" : "Add Medical Record"}</DialogTitle>
                    <DialogDescription>{record ? "Update the details of the medical record." : "Fill in the details for the new medical record."}</DialogDescription>
                </DialogHeader>
                <div className="space-y-4">
                    <div>
                        <Label htmlFor="animalId">Animal</Label>
                        <select
                            id="animalId"
                            name="animalId"
                            value={formData.animalId ?? ""}
                            onChange={handleChange}
                            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
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
                    <div>
                        <Label htmlFor="date">Date</Label>
                        <DatePicker
                            id="date"
                            selectedDate={formData.date ? new Date(formData.date) : null}
                            onDateChange={handleDateChange}
                            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
                        />
                    </div>
                    <div>
                        <Label htmlFor="diagnosis">Diagnosis</Label>
                        <Textarea
                            id="diagnosis"
                            name="diagnosis"
                            value={formData.diagnosis}
                            onChange={handleChange}
                            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
                        />
                    </div>
                    <div>
                        <Label htmlFor="treatment">Treatment</Label>
                        <Textarea
                            id="treatment"
                            name="treatment"
                            value={formData.treatment}
                            onChange={handleChange}
                            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
                        />
                    </div>
                    <div>
                        <Label htmlFor="cost">Cost</Label>
                        <Input
                            type="number"
                            id="cost"
                            name="cost"
                            value={formData.cost}
                            onChange={handleChange}
                            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
                        />
                    </div>
                    {error && <p className="text-red-600">{error}</p>}
                </div>
                <DialogFooter>
                    <Button variant="outline" onClick={onClose} disabled={isSubmitting}>
                        Cancel
                    </Button>
                    <Button onClick={handleSubmit} disabled={isSubmitting}>
                        {isSubmitting ? "Submitting..." : record ? "Update Record" : "Add Record"}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}