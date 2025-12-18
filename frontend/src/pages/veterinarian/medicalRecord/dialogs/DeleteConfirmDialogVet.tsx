import { useState } from "react";
import type { MedicalRecord } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { ExclamationTriangleIcon } from "@heroicons/react/24/outline";

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
      <DialogContent className="max-w-md w-[95vw] sm:w-full">
        <DialogHeader>
          <div className="flex items-center gap-3 md:gap-4">
            <div className="shrink-0 w-10 h-10 md:w-12 md:h-12 rounded-full bg-red-100 flex items-center justify-center">
              <ExclamationTriangleIcon className="w-5 h-5 md:w-6 md:h-6 text-red-600" />
            </div>
            <div>
              <DialogTitle className="text-base md:text-lg">Delete Medical Record</DialogTitle>
              <DialogDescription className="mt-1 text-sm">This action cannot be undone.</DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <div className="py-4">
          <div className="bg-red-50 border border-red-100 p-4 rounded-lg">
            <div className="flex gap-4">
              {record.animal?.imageUrl && (
                <div className="w-16 h-16 rounded-lg overflow-hidden shrink-0">
                  <img src={record.animal.imageUrl} alt={record.animal.name} className="w-full h-full object-cover" />
                </div>
              )}
              <div className="space-y-1">
                <p className="font-semibold text-gray-900">Record #{record.id}</p>
                <p className="text-sm text-gray-600">
                  Patient: <span className="font-medium">{record.animal?.name || "Unknown"}</span>
                </p>
                <p className="text-sm text-gray-600">
                  Date: <span className="font-medium">{new Date(record.date).toLocaleDateString()}</span>
                </p>
                <p className="text-sm text-gray-600">
                  Diagnosis: <span className="font-medium">{record.diagnosis}</span>
                </p>
              </div>
            </div>
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={onClose} disabled={isDeleting}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleConfirm} disabled={isDeleting}>
            {isDeleting ? "Deleting..." : "Delete Record"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
