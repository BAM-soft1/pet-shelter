import { useState, useEffect } from "react";
import MainLayout from "../../components/layout/MainLayout";
import AnimalDetailModal from "./AnimalDetailModal";
import type { Animal } from "../../types/types";
import { AnimalService } from "../../api/animals";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { getErrorMessage } from "../../services/fetchUtils";
import calculateAge from "@/utils/calculateAge";

export default function AnimalOverview() {
  const [animals, setAnimals] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedAnimal, setSelectedAnimal] = useState<Animal | null>(null);
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    const fetchAnimals = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await AnimalService.getAnimals();
        // Filter to only show active animals with status "available"
        const availableAnimals = data.filter((animal) => animal.isActive && animal.status.toLowerCase() === "available");
        setAnimals(availableAnimals);
      } catch (err) {
        setError(getErrorMessage(err));
      } finally {
        setLoading(false);
      }
    };

    fetchAnimals();
  }, []);

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
      <div>
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
                    <Badge variant="outline" className={`capitalize ${
                    animal.status === "AVAILABLE" 
                      ? "bg-green-100 text-green-800 border-green-300" 
                      : ""
                  }`}
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

        <AnimalDetailModal animal={selectedAnimal} isOpen={isOpen} onClose={closeModal} />
      </div>
    </MainLayout>
  );
}
