import { useState, useEffect } from "react";
import type { Animal, Species, Breed, AnimalRequest } from "@/types/types";
import { AnimalService } from "@/api/animals";
import { SpeciesService, BreedService } from "@/api/species";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { PlusIcon, PencilIcon, TrashIcon } from "@heroicons/react/24/outline";
import { getErrorMessage } from "@/services/fetchUtils";
import calculateAge from "@/utils/calculateAge";
import AnimalFormDialog from "./dialogs/AnimalFormDialog";
import DeleteConfirmDialog from "./dialogs/DeleteConfirmDialog";

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

export default function AdminAnimals() {
  const [animals, setAnimals] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Modal states
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [selectedAnimal, setSelectedAnimal] = useState<Animal | null>(null);

  useEffect(() => {
    fetchAnimals();
  }, []);

  const fetchAnimals = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await AnimalService.getAnimals();
      setAnimals(data);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (formData: AnimalFormData) => {
    if (!formData.speciesId) return;

    // Get species and breed objects
    const species: Species = await SpeciesService.getSpeciesById(formData.speciesId);
    const breed: Breed | null = formData.breedId ? await BreedService.getBreedById(formData.breedId) : null;

    const animalData: AnimalRequest = {
      name: formData.name,
      sex: formData.sex,
      species,
      breed,
      birthDate: formData.birthDate,
      intakeDate: formData.intakeDate || new Date().toISOString().split('T')[0],
      status: formData.status,
      price: formData.price,
      imageUrl: formData.imageUrl,
    };

    await AnimalService.createAnimal(animalData);
    await fetchAnimals();
  };

  const handleUpdate = async (formData: AnimalFormData) => {
    if (!selectedAnimal || !formData.speciesId) return;

    // Get species and breed objects
    const species: Species = await SpeciesService.getSpeciesById(formData.speciesId);
    const breed: Breed | null = formData.breedId ? await BreedService.getBreedById(formData.breedId) : null;

    const animalData: Partial<Animal> = {
      name: formData.name,
      sex: formData.sex,
      species,
      breed,
      birthDate: formData.birthDate,
      intakeDate: formData.intakeDate,
      status: formData.status,
      price: formData.price,
      imageUrl: formData.imageUrl,
    };

    await AnimalService.updateAnimal(selectedAnimal.id, animalData);
    await fetchAnimals();
  };

  const handleDelete = async () => {
    if (!selectedAnimal) return;
    await AnimalService.deleteAnimal(selectedAnimal.id);
    await fetchAnimals();
  };

  const openEditDialog = (animal: Animal) => {
    setSelectedAnimal(animal);
    setIsEditOpen(true);
  };

  const openDeleteDialog = (animal: Animal) => {
    setSelectedAnimal(animal);
    setIsDeleteOpen(true);
  };

  if (loading) {
    return (
      <div className="text-center py-12">
        <p className="text-muted-foreground">Loading animals...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center py-12">
        <p className="text-destructive">Error loading animals: {error}</p>
        <button onClick={fetchAnimals} className="mt-4 text-indigo-600 hover:text-indigo-700 underline">
          Try again
        </button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-3xl font-bold text-gray-800">Animal Management</h2>
        </div>
        <button
          onClick={() => setIsCreateOpen(true)}
          className="bg-indigo-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition-colors flex items-center gap-2"
        >
          <PlusIcon className="size-5" />
          Add Animal
        </button>
      </div>

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-xl font-bold text-gray-800">All Animals ({animals.length})</h3>
        </div>
        <div className="p-6">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>ID</TableHead>
                <TableHead>Name</TableHead>
                <TableHead>Species</TableHead>
                <TableHead>Breed</TableHead>
                <TableHead>Age</TableHead>
                <TableHead>Sex</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Price</TableHead>
                <TableHead>Active</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {animals.map((animal) => {
                const age = calculateAge(animal.birthDate);
                return (
                  <TableRow key={animal.id}>
                    <TableCell className="font-medium">{animal.id}</TableCell>
                    <TableCell className="font-medium">{animal.name}</TableCell>
                    <TableCell>{animal.species.name}</TableCell>
                    <TableCell>{animal.breed?.name || "—"}</TableCell>
                    <TableCell>
                      {age} {age === 1 ? "yr" : "yrs"}
                    </TableCell>
                    <TableCell className="capitalize">{animal.sex}</TableCell>
                    <TableCell>
                      <Badge variant="outline" className="capitalize">
                        {animal.status}
                      </Badge>
                    </TableCell>
                    <TableCell>${animal.price}</TableCell>
                    <TableCell>
                      {animal.isActive ? (
                        <Badge className="bg-green-500 hover:bg-green-600">Active</Badge>
                      ) : (
                        <Badge className="bg-gray-500 hover:bg-gray-600">Inactive</Badge>
                      )}
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <button
                          onClick={() => openEditDialog(animal)}
                          className="p-2 text-gray-600 hover:text-indigo-600 hover:bg-gray-100 rounded transition-colors"
                        >
                          <PencilIcon className="size-5" />
                        </button>
                        <button
                          onClick={() => openDeleteDialog(animal)}
                          className="p-2 text-gray-600 hover:text-red-600 hover:bg-red-50 rounded transition-colors"
                        >
                          <TrashIcon className="size-5" />
                        </button>
                      </div>
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </div>
      </div>

      {/* Modals */}
      <AnimalFormDialog
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        onSubmit={handleCreate}
        title="Add New Animal"
        description="Fill in the details to add a new animal to the shelter."
      />

      <AnimalFormDialog
        animal={selectedAnimal}
        isOpen={isEditOpen}
        onClose={() => {
          setIsEditOpen(false);
          setSelectedAnimal(null);
        }}
        onSubmit={handleUpdate}
        title="Edit Animal"
        description="Update the animal's information."
      />

      <DeleteConfirmDialog
        animal={selectedAnimal}
        isOpen={isDeleteOpen}
        onClose={() => {
          setIsDeleteOpen(false);
          setSelectedAnimal(null);
        }}
        onConfirm={handleDelete}
      />
    </div>
  );
}
