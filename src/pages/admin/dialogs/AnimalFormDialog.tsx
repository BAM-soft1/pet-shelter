import { useState, useEffect } from "react";
import type { Animal, Species, Breed } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { SpeciesService, BreedService } from "@/api/species";

type AnimalFormData = {
  name: string;
  sex: string;
  birthDate: string;
  intakeDate?: string;
  status: string;
  price: number;
  imageUrl: string;
  speciesId: number | null;
  breedId: number | null;
};

type AnimalFormDialogProps = {
  animal?: Animal | null;
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (animalData: AnimalFormData) => Promise<void>;
  title: string;
  description: string;
};

export default function AnimalFormDialog({ animal, isOpen, onClose, onSubmit, title, description }: AnimalFormDialogProps) {
  const [formData, setFormData] = useState({
    name: "",
    speciesId: null as number | null,
    breedId: null as number | null,
    birthDate: "",
    sex: "unknown",
    status: "available",
    price: 0,
    imageUrl: "",
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [species, setSpecies] = useState<Species[]>([]);
  const [breeds, setBreeds] = useState<Breed[]>([]);
  const [loadingSpecies, setLoadingSpecies] = useState(false);

  // Load species when dialog opens
  useEffect(() => {
    if (isOpen) {
      loadSpecies();
    }
  }, [isOpen]);

  // Load breeds when species changes
  useEffect(() => {
    if (formData.speciesId) {
      loadBreeds(formData.speciesId);
    } else {
      setBreeds([]);
    }
  }, [formData.speciesId]);

  // Initialize form data
  useEffect(() => {
    if (animal) {
      setFormData({
        name: animal.name,
        speciesId: animal.species.id,
        breedId: animal.breed?.id || null,
        birthDate: animal.birthDate.split("T")[0], // Convert to YYYY-MM-DD format
        sex: animal.sex,
        status: animal.status,
        price: animal.price,
        imageUrl: animal.imageUrl || "",
      });
    } else {
      // Reset form for create mode
      setFormData({
        name: "",
        speciesId: null,
        breedId: null,
        birthDate: "",
        sex: "unknown",
        status: "available",
        price: 0,
        imageUrl: "",
      });
    }
    setError("");
  }, [animal, isOpen]);

  const loadSpecies = async () => {
    try {
      setLoadingSpecies(true);
      const data = await SpeciesService.getAllSpecies();
      setSpecies(data);
    } catch (err) {
      console.error("Failed to load species:", err);
      setError("Failed to load species");
    } finally {
      setLoadingSpecies(false);
    }
  };

  const loadBreeds = async (speciesId: number) => {
    try {
      const data = await BreedService.getBreedsBySpecies(speciesId);
      setBreeds(data);
    } catch (err) {
      console.error("Failed to load breeds:", err);
      setBreeds([]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    if (!formData.speciesId) {
      setError("Please select a species");
      return;
    }

    setIsSubmitting(true);

    try {
      const animalData: AnimalFormData = {
        name: formData.name,
        sex: formData.sex,
        birthDate: formData.birthDate,
        intakeDate: animal?.intakeDate || new Date().toISOString().split("T")[0],
        status: formData.status,
        price: formData.price,
        imageUrl: formData.imageUrl,
        speciesId: formData.speciesId,
        breedId: formData.breedId,
      };

      await onSubmit(animalData);
      onClose();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to save animal");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
          <DialogDescription>{description}</DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit}>
          <div className="space-y-4 py-4">
            {error && <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm">{error}</div>}

            {/* Name */}
            <div className="space-y-2">
              <Label htmlFor="name">Name *</Label>
              <Input
                id="name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                required
                placeholder="e.g., Buddy"
              />
            </div>

            {/* Species and Breed */}
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="species">Species *</Label>
                <select
                  id="species"
                  value={formData.speciesId || ""}
                  onChange={(e) => {
                    const speciesId = e.target.value ? parseInt(e.target.value) : null;
                    setFormData({ ...formData, speciesId, breedId: null });
                  }}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  required
                  disabled={loadingSpecies}
                >
                  <option value="">Select a species</option>
                  {species.map((s) => (
                    <option key={s.id} value={s.id}>
                      {s.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="breed">Breed</Label>
                <select
                  id="breed"
                  value={formData.breedId || ""}
                  onChange={(e) => {
                    const breedId = e.target.value ? parseInt(e.target.value) : null;
                    setFormData({ ...formData, breedId });
                  }}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  disabled={!formData.speciesId || breeds.length === 0}
                >
                  <option value="">Select a breed (optional)</option>
                  {breeds.map((b) => (
                    <option key={b.id} value={b.id}>
                      {b.name}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            {/* Birth Date */}
            <div className="space-y-2">
              <Label htmlFor="birthDate">Birth Date *</Label>
              <Input
                id="birthDate"
                type="date"
                value={formData.birthDate}
                onChange={(e) => setFormData({ ...formData, birthDate: e.target.value })}
                required
              />
            </div>

            {/* Sex and Status */}
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="sex">Sex *</Label>
                <select
                  id="sex"
                  value={formData.sex}
                  onChange={(e) => setFormData({ ...formData, sex: e.target.value as "male" | "female" | "unknown" })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  required
                >
                  <option value="male">Male</option>
                  <option value="female">Female</option>
                  <option value="unknown">Unknown</option>
                </select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="status">Status *</Label>
                <select
                  id="status"
                  value={formData.status}
                  onChange={(e) => setFormData({ ...formData, status: e.target.value as Animal["status"] })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  required
                >
                  <option value="available">Available</option>
                  <option value="adopted">Adopted</option>
                  <option value="fostered">Fostered</option>
                  <option value="deceased">Deceased</option>
                </select>
              </div>
            </div>

            {/* Price */}
            <div className="space-y-2">
              <Label htmlFor="price">Adoption Fee ($) *</Label>
              <Input
                id="price"
                type="number"
                min="0"
                step="0.01"
                value={formData.price}
                onChange={(e) => setFormData({ ...formData, price: parseFloat(e.target.value) || 0 })}
                required
              />
              <p className="text-xs text-gray-500">
                Note: Active status is automatically set based on the animal's status
              </p>
            </div>

            {/* Image URL */}
            <div className="space-y-2">
              <Label htmlFor="imageUrl">Image URL</Label>
              <Input
                id="imageUrl"
                type="url"
                value={formData.imageUrl}
                onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
                placeholder="https://example.com/image.jpg"
              />
              {formData.imageUrl && (
                <div className="mt-2">
                  <img
                    src={formData.imageUrl}
                    alt="Preview"
                    className="w-32 h-32 object-cover rounded-lg border border-gray-200"
                    onError={(e) => {
                      e.currentTarget.style.display = "none";
                    }}
                  />
                </div>
              )}
            </div>
          </div>

          <DialogFooter>
            <Button type="button" variant="outline" onClick={onClose} disabled={isSubmitting}>
              Cancel
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? "Saving..." : animal ? "Update Animal" : "Create Animal"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
