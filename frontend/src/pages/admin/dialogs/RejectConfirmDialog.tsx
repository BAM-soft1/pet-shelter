import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import type { AdoptionApplication } from "@/types/types";

type RejectConfirmDialogProps = {
  application: AdoptionApplication | null;
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => Promise<void>;
};

export default function RejectConfirmDialog({ application, isOpen, onClose, onConfirm }: RejectConfirmDialogProps) {
  if (!application) return null;

  const handleConfirm = async () => {
    await onConfirm();
    onClose();
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="w-[95vw] sm:max-w-lg">
        <DialogHeader>
          <DialogTitle className="text-lg md:text-xl">Reject Application</DialogTitle>
          <DialogDescription className="text-sm">Are you sure you want to reject this adoption application?</DialogDescription>
        </DialogHeader>

        <div className="space-y-4 py-4">
          <div className="bg-muted/50 rounded-lg p-4 space-y-3">
            <div>
              <p className="text-sm text-muted-foreground">Applicant</p>
              <p className="font-medium">
                {application.user.firstName} {application.user.lastName}
              </p>
            </div>
            <div>
              <p className="text-sm text-muted-foreground">Animal</p>
              <p className="font-medium">{application.animal.name}</p>
            </div>
            <div>
              <p className="text-sm text-muted-foreground">Email</p>
              <p className="font-medium">{application.user.email}</p>
            </div>
          </div>

          <div className="bg-red-50 dark:bg-red-950/20 border border-red-200 dark:border-red-900 rounded-lg p-3">
            <p className="text-sm text-red-800 dark:text-red-200">This action will reject the application and notify the applicant.</p>
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleConfirm}>
            Reject Application
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
