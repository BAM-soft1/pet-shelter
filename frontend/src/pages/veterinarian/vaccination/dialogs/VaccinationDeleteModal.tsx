import { useState } from "react";
import type { Vaccination } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { ExclamationTriangleIcon } from "@heroicons/react/24/outline";

interface VaccinationDeleteModalProps {
  vaccination: Vaccination;
  isOpen: boolean;
  onClose: () => void;
  onDelete: (vaccinationId: number) => Promise<void>;
}

export default function VaccinationDeleteModal({ vaccination, isOpen, onClose, onDelete }: VaccinationDeleteModalProps) {
  const [isDeleting, setIsDeleting] = useState(false);

  const handleDelete = async () => {
    setIsDeleting(true);
    try {
      await onDelete(vaccination.id);
      onClose();
    } catch (error) {
      console.error("Failed to delete vaccination:", error);
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="w-[95vw] sm:max-w-lg">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-lg md:text-xl">
            <ExclamationTriangleIcon className="h-5 w-5 md:h-6 md:w-6 text-red-600" />
            Delete Vaccination
          </DialogTitle>
          <DialogDescription className="text-sm">
            Are you sure you want to delete the vaccination record for <strong>{vaccination.animal?.name ?? "this animal"}</strong>? This action
            cannot be undone.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter className="mt-4">
          <Button variant="outline" onClick={onClose} disabled={isDeleting}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleDelete} disabled={isDeleting}>
            {isDeleting ? "Deleting..." : "Delete"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
