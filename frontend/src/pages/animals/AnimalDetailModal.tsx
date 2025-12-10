import type { Animal } from "../../types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { useNavigate } from "react-router-dom";
import calculateAge from "@/utils/calculateAge";

type AnimalDetailModalProps = {
  animal: Animal | null;
  isOpen: boolean;
  onClose: () => void;
};

export default function AnimalDetailModal({ animal, isOpen, onClose }: AnimalDetailModalProps) {
  if (!animal) return null;

  const age = calculateAge(animal.birthDate);
  const navigate = useNavigate();

    const handleApplyForAdoption = () => {
    navigate("/animal-detailed", { state: { animal } });
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-3xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="text-3xl">{animal.name}</DialogTitle>
          <DialogDescription>
            {animal.breed?.name || animal.species.name} • {age} {age === 1 ? "year" : "years"} old
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6">
          {/* Image */}
          <div className="relative h-96 overflow-hidden rounded-lg bg-muted">
            {animal.imageUrl ? (
              <img src={animal.imageUrl} alt={animal.name} className="w-full h-full object-cover object-center" />
            ) : (
              <div className="w-full h-full flex items-center justify-center bg-linear-to-br from-primary/10 to-primary/5">
                <span className="text-6xl font-bold text-primary/60">{animal.species.name}</span>
              </div>
            )}
          </div>

          {/* Price */}
          <div>
            <span className="text-3xl font-bold text-primary">${animal.price}</span>
          </div>

          {/* Details Grid */}
          <div className="grid md:grid-cols-2 gap-6">
            <div className="space-y-4">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Sex</p>
                <Badge variant="secondary" className="capitalize mt-1">
                  {animal.sex}
                </Badge>
              </div>
              <div>
                <p className="text-sm font-medium text-muted-foreground">Status</p>
                <Badge 
                  variant="outline" 
                  className={`capitalize mt-1 ${
                    animal.status === "AVAILABLE" 
                      ? "bg-green-100 text-green-800 border-green-300" 
                      : ""
                  }`}
                >
                  {animal.status}
                </Badge>
              </div>
              <div>
                <p className="text-sm font-medium text-muted-foreground">Intake Date</p>
                <p className="text-sm mt-1">{new Date(animal.intakeDate).toLocaleDateString()}</p>
              </div>
            </div>

            <div className="space-y-4">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Birth Date</p>
                <p className="text-sm mt-1">{new Date(animal.birthDate).toLocaleDateString()}</p>
              </div>
              <div>
                <p className="text-sm font-medium text-muted-foreground">Species</p>
                <p className="text-sm mt-1">{animal.species.name}</p>
              </div>
              <div>
                <p className="text-sm font-medium text-muted-foreground">Breed</p>
                <p className="text-sm mt-1">{animal.breed?.name || "Mixed breed"}</p>
              </div>
            </div>
          </div>
        </div>

        <DialogFooter className="flex-col sm:flex-row gap-2 w-full">
          <Button variant="outline" onClick={onClose} className="flex-1 w-full">
            Close
          </Button>
          <Button className="flex-1 w-full cursor-pointer" onClick={handleApplyForAdoption}>Apply for Adoption</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
