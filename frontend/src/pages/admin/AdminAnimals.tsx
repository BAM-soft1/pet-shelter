import { useState, useEffect, useCallback } from "react";
import type { Animal, AnimalRequest } from "@/types/types";
import { AnimalService } from "@/api/animals";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import PaginationControls from "@/components/ui/PaginationControls";
import SearchAndFilter from "@/components/ui/SearchAndFilter";
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

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Search state
  const [searchInput, setSearchInput] = useState("");
  const [searchTerm, setSearchTerm] = useState("");

  // Filter states
  const [sexFilter, setSexFilter] = useState<string>("all");
  const [ageFilter, setAgeFilter] = useState<string>("all");
  const [vaccinationStatusFilter, setVaccinationStatusFilter] = useState<boolean | undefined>(undefined);

  // Modal states
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [selectedAnimal, setSelectedAnimal] = useState<Animal | null>(null);

  const getAgeRange = (filter: string): { minAge?: number; maxAge?: number } => {
    switch (filter) {
      case "puppy":
        return { minAge: 0, maxAge: 2 };
      case "young":
        return { minAge: 2, maxAge: 7 };
      case "adult":
        return { minAge: 7, maxAge: 12 };
      case "senior":
        return { minAge: 12 };
      default:
        return {};
    }
  };

  const fetchAnimals = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const ageRange = getAgeRange(ageFilter);
      const pageResponse = await AnimalService.getAnimals(
        currentPage,
        8,
        "name",
        "asc",
        undefined,
        undefined,
        vaccinationStatusFilter,
        sexFilter !== "all" ? sexFilter : undefined,
        ageRange.minAge,
        ageRange.maxAge,
        searchTerm
      );
      setAnimals(pageResponse.content);
      setTotalPages(pageResponse.totalPages);
      setTotalElements(pageResponse.totalElements);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, [currentPage, searchTerm, sexFilter, ageFilter, vaccinationStatusFilter]);

  useEffect(() => {
    fetchAnimals();
  }, [fetchAnimals]);

  const handleCreate = async (formData: AnimalFormData) => {
    if (!formData.speciesId) return;

    const animalData: AnimalRequest = {
      name: formData.name,
      sex: formData.sex,
      speciesId: formData.speciesId,
      breedId: formData.breedId,
      birthDate: formData.birthDate,
      intakeDate: formData.intakeDate || new Date().toISOString().split("T")[0],
      status: formData.status,
      price: formData.price,
      imageUrl: formData.imageUrl,
      isActive: true,
    };

    await AnimalService.createAnimal(animalData);
    await fetchAnimals();
  };

  const handleUpdate = async (formData: AnimalFormData) => {
    if (!selectedAnimal || !formData.speciesId) return;

    const animalData: AnimalRequest = {
      name: formData.name,
      sex: formData.sex,
      speciesId: formData.speciesId,
      breedId: formData.breedId,
      birthDate: formData.birthDate,
      intakeDate: formData.intakeDate || new Date().toISOString().split("T")[0],
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

  const handleSearch = () => {
    setSearchTerm(searchInput);
    setCurrentPage(0);
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
        <div className="px-6 py-4 border-b border-gray-200 space-y-4">
          <SearchAndFilter
            searchValue={searchInput}
            onSearchChange={setSearchInput}
            searchPlaceholder="Search animals by name, species, or breed..."
            onClearFilters={() => {
              setSearchInput("");
              setSearchTerm("");
              setSexFilter("all");
              setAgeFilter("all");
              setVaccinationStatusFilter(undefined);
              setCurrentPage(0);
            }}
            onSearchSubmit={handleSearch}
            showSearchButton={true}
            showClearButton={true}
            totalCount={totalElements}
            filteredCount={totalElements}
            showCounts={true}
            showFilters={false}
          />

          <div className="flex gap-4 flex-wrap">
            <div>
              <label className="block text-sm font-medium mb-2">Sex</label>
              <div className="flex gap-2">
                <Button variant={sexFilter === "all" ? "default" : "outline"} size="sm" onClick={() => setSexFilter("all")}>
                  All
                </Button>
                <Button variant={sexFilter === "male" ? "default" : "outline"} size="sm" onClick={() => setSexFilter("male")}>
                  Male
                </Button>
                <Button variant={sexFilter === "female" ? "default" : "outline"} size="sm" onClick={() => setSexFilter("female")}>
                  Female
                </Button>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Age</label>
              <Select value={ageFilter} onValueChange={setAgeFilter}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue placeholder="All ages" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All ages</SelectItem>
                  <SelectItem value="puppy">Puppy/Kitten (0-2)</SelectItem>
                  <SelectItem value="young">Young (2-7)</SelectItem>
                  <SelectItem value="adult">Adult (7-12)</SelectItem>
                  <SelectItem value="senior">Senior (12+)</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Vaccination Status</label>
              <div className="flex gap-2">
                <Button
                  variant={vaccinationStatusFilter === undefined ? "default" : "outline"}
                  size="sm"
                  onClick={() => setVaccinationStatusFilter(undefined)}
                >
                  All
                </Button>
                <Button variant={vaccinationStatusFilter === true ? "default" : "outline"} size="sm" onClick={() => setVaccinationStatusFilter(true)}>
                  Fully Vaccinated
                </Button>
                <Button
                  variant={vaccinationStatusFilter === false ? "default" : "outline"}
                  size="sm"
                  onClick={() => setVaccinationStatusFilter(false)}
                >
                  Needs Vaccinations
                </Button>
              </div>
            </div>
          </div>
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

      <PaginationControls currentPage={currentPage} totalPages={totalPages} totalElements={totalElements} onPageChange={setCurrentPage} />

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
