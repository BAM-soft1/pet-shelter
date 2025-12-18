import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import type { AdoptionApplication } from "@/types/types";

type ApproveConfirmDialogProps = {
  application: AdoptionApplication | null;
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => Promise<void>;
};

export default function ApproveConfirmDialog({ application, isOpen, onClose, onConfirm }: ApproveConfirmDialogProps) {
  if (!application) return null;

  const handleConfirm = async () => {
    await onConfirm();
    onClose();
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="w-[95vw] sm:max-w-lg">
        <DialogHeader>
          <DialogTitle className="text-lg md:text-xl">Approve Application</DialogTitle>
          <DialogDescription className="text-sm">Are you sure you want to approve this adoption application?</DialogDescription>
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

          <div className="bg-green-50 dark:bg-green-950/20 border border-green-200 dark:border-green-900 rounded-lg p-3">
            <p className="text-sm text-green-800 dark:text-green-200">This will approve the application and notify the applicant.</p>
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button variant="default" className="bg-green-600 hover:bg-green-700" onClick={handleConfirm}>
            Approve Application
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
