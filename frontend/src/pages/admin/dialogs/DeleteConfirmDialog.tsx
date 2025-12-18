import { useState } from "react";
import type { Animal } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import calculateAge from "@/utils/calculateAge";

type DeleteConfirmDialogProps = {
  animal: Animal | null;
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => Promise<void>;
};

export default function DeleteConfirmDialog({ animal, isOpen, onClose, onConfirm }: DeleteConfirmDialogProps) {
  const [isDeleting, setIsDeleting] = useState(false);

  const handleConfirm = async () => {
    setIsDeleting(true);
    try {
      await onConfirm();
      onClose();
    } catch (error) {
      console.error("Failed to delete animal:", error);
    } finally {
      setIsDeleting(false);
    }
  };

  if (!animal) return null;

  const age = calculateAge(animal.birthDate);

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-md w-[95vw] sm:w-full">
        <DialogHeader>
          <DialogTitle className="text-lg md:text-xl">Delete Animal</DialogTitle>
          <DialogDescription className="text-sm">Are you sure you want to delete this animal? This action cannot be undone.</DialogDescription>
        </DialogHeader>

        <div className="py-4 space-y-3">
          <div className="bg-gray-50 p-4 rounded-lg space-y-2">
            <div className="flex justify-between items-start">
              <div>
                <p className="font-semibold text-lg">{animal.name}</p>
                <p className="text-sm text-gray-600">
                  {animal.breed?.name || animal.species.name} • {age} {age === 1 ? "year" : "years"} old
                </p>
              </div>
              <Badge variant="secondary" className="capitalize">
                {animal.sex}
              </Badge>
            </div>
            <div className="flex gap-2 items-center">
              <Badge variant="outline" className="capitalize">
                {animal.status}
              </Badge>
              {animal.isActive && <Badge className="bg-green-500 hover:bg-green-600">Active</Badge>}
            </div>
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={onClose} disabled={isDeleting}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleConfirm} disabled={isDeleting} className="bg-red-600 hover:bg-red-700">
            {isDeleting ? "Deleting..." : "Delete Animal"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
