import { useState } from "react";
import type { VaccinationType } from "@/types/types";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { ExclamationTriangleIcon } from "@heroicons/react/24/outline";

interface VaccinationTypeDeleteModalProps {
    vaccinationType: VaccinationType;
    isOpen: boolean;
    onClose: () => void;
    onDelete: (vaccinationTypeId: number) => Promise<void>;
}

export default function VaccinationTypeDeleteModal({
    vaccinationType,
    isOpen,
    onClose,
    onDelete,
}: VaccinationTypeDeleteModalProps) {
    const [isDeleting, setIsDeleting] = useState(false);

    const handleDelete = async () => {
        setIsDeleting(true);
        try {
            await onDelete(vaccinationType.id);
            onClose();
        } catch (error) {
            console.error("Failed to delete vaccination type:", error);
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle className="flex items-center gap-2">
                        <ExclamationTriangleIcon className="h-6 w-6 text-red-600" />
                        Delete Vaccination Type
                    </DialogTitle>
                    <DialogDescription>
                        Are you sure you want to delete the vaccination type{" "}
                        <strong>{vaccinationType.vaccineName}</strong>? This action cannot be undone.
                    </DialogDescription>
                </DialogHeader>
                <DialogFooter className="mt-4">
                    <Button variant="outline" onClick={onClose} disabled={isDeleting}>
                        Cancel
                    </Button>
                    <Button
                        variant="destructive"
                        onClick={handleDelete}
                        disabled={isDeleting}
                    >
                        {isDeleting ? "Deleting..." : "Delete"}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}