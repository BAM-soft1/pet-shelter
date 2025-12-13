import { useState, useEffect } from "react";
import MainLayout from "../../components/layout/MainLayout";
import AnimalDetailModal from "./AnimalDetailModal";
import SearchAndFilter from "@/components/ui/SearchAndFilter";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import type { Animal } from "../../types/types";
import { AnimalService } from "../../api/animals";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import PaginationControls from "@/components/ui/PaginationControls";
import { getErrorMessage } from "../../services/fetchUtils";
import calculateAge from "@/utils/calculateAge";

export default function AnimalOverview() {
  const [animals, setAnimals] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedAnimal, setSelectedAnimal] = useState<Animal | null>(null);
  const [isOpen, setIsOpen] = useState(false);

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Search and filter state
  const [searchInput, setSearchInput] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [sexFilter, setSexFilter] = useState("all");
  const [ageFilter, setAgeFilter] = useState("all");

  const handleSearch = () => {
    setSearchTerm(searchInput);
    setCurrentPage(0);
  };

  const clearFilters = () => {
    setSearchInput("");
    setSearchTerm("");
    setSexFilter("all");
    setAgeFilter("all");
    setCurrentPage(0);
  };

  // Map age filter to min/max values
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

  useEffect(() => {
    const fetchAnimals = async () => {
      try {
        setLoading(true);
        setError(null);
        const ageRange = getAgeRange(ageFilter);
        // Always filter for available, active animals with required vaccinations
        const pageResponse = await AnimalService.getAnimals(
          currentPage,
          8,
          "name",
          "asc",
          "available",
          true,
          true,
          sexFilter !== "all" ? sexFilter : undefined,
          ageRange.minAge,
          ageRange.maxAge,
          searchTerm || undefined
        );
        setAnimals(pageResponse.content);
        setTotalPages(pageResponse.totalPages);
        setTotalElements(pageResponse.totalElements);
      } catch (err) {
        setError(getErrorMessage(err));
      } finally {
        setLoading(false);
      }
    };

    fetchAnimals();
  }, [currentPage, searchTerm, sexFilter, ageFilter]);

  const openModal = (animal: Animal) => {
    setSelectedAnimal(animal);
    setIsOpen(true);
  };

  const closeModal = () => {
    setIsOpen(false);
    setTimeout(() => setSelectedAnimal(null), 200);
  };

  if (loading) {
    return (
      <MainLayout>
        <div className="text-center py-12">
          <p className="text-muted-foreground">Loading animals...</p>
        </div>
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout>
        <div className="text-center py-12">
          <p className="text-destructive">Error loading animals: {error}</p>
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="space-y-6">
        <h2 className="text-3xl font-bold">Available Animals</h2>

        <SearchAndFilter
          searchValue={searchInput}
          onSearchChange={setSearchInput}
          searchPlaceholder="Search by name..."
          onClearFilters={clearFilters}
          showFilters={false}
          showSearchButton={true}
          onSearchSubmit={handleSearch}
          showCounts={true}
          totalCount={totalElements}
          filteredCount={animals.length}
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
        </div>

        <div className="grid md:grid-cols-3 lg:grid-cols-4 gap-4">
          {animals.map((animal) => {
            const age = calculateAge(animal.birthDate);
            return (
              <Card key={animal.id} className="overflow-hidden cursor-pointer transition-all hover:shadow-lg" onClick={() => openModal(animal)}>
                <div className="relative aspect-4/3 overflow-hidden bg-muted">
                  {animal.imageUrl ? (
                    <img src={animal.imageUrl} alt={animal.name} className="w-full h-full object-cover object-center" />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center bg-linear-to-br from-primary/10 to-primary/5">
                      <span className="text-4xl font-bold text-primary/60">{animal.species.name}</span>
                    </div>
                  )}
                </div>

                <CardHeader className="pb-3">
                  <div className="flex justify-between items-start gap-2">
                    <h3 className="text-xl font-bold">{animal.name}</h3>
                    <Badge variant="secondary" className="capitalize">
                      {animal.sex}
                    </Badge>
                  </div>
                  <p className="text-sm text-muted-foreground">
                    {animal.breed?.name || animal.species.name} • {age} {age === 1 ? "year" : "years"} old
                  </p>
                </CardHeader>

                <CardContent className="pb-3">
                  <div className="flex items-center gap-2">
                    <Badge
                      variant="outline"
                      className={`capitalize ${animal.status === "AVAILABLE" ? "bg-green-100 text-green-800 border-green-300" : ""}`}
                    >
                      {animal.status}
                    </Badge>
                  </div>
                </CardContent>

                <CardFooter className="pt-3">
                  <div className="w-full flex justify-between items-center">
                    <span className="text-2xl font-bold text-primary">${animal.price}</span>
                    <span className="text-xs text-muted-foreground">Click for details</span>
                  </div>
                </CardFooter>
              </Card>
            );
          })}
        </div>

        {animals.length === 0 && (
          <div className="text-center py-12">
            <p className="text-muted-foreground">No animals available at the moment.</p>
          </div>
        )}

        <PaginationControls
          currentPage={currentPage}
          totalPages={totalPages}
          totalElements={totalElements}
          onPageChange={setCurrentPage}
        />

        <AnimalDetailModal animal={selectedAnimal} isOpen={isOpen} onClose={closeModal} />
      </div>
    </MainLayout>
  );
}
