import { useState } from "react";
import type { MedicalRecord } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

type DeleteConfirmDialogVetProps = {
  record: MedicalRecord | null;
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => Promise<void>;
};

export default function DeleteConfirmDialogVet({ record, isOpen, onClose, onConfirm }: DeleteConfirmDialogVetProps) {
  const [isDeleting, setIsDeleting] = useState(false);

  const handleConfirm = async () => {
    setIsDeleting(true);
    try {
      await onConfirm();
      onClose();
    } catch (error) {
      console.error("Failed to delete medical record:", error);
    } finally {
      setIsDeleting(false);
    }
  };

  if (!record) return null;

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle>Delete Medical Record</DialogTitle>
          <DialogDescription>Are you sure you want to delete this medical record? This action cannot be undone.</DialogDescription>
        </DialogHeader>
        <div className="py-4 space-y-3">
          <div className="bg-gray-50 p-4 rounded-lg space-y-2">
            <div className="flex justify-between items-start">
              <div>
                <p className="font-semibold text-lg">Record ID: {record.id}</p>
                <p className="text-sm text-gray-600">Animal: {record.animal.name}</p>
                <p className="text-sm text-gray-600">Date: {new Date(record.date).toLocaleDateString()}</p>
              </div>
            </div>
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} disabled={isDeleting}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleConfirm} disabled={isDeleting}>
            {isDeleting ? "Deleting..." : "Delete"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}