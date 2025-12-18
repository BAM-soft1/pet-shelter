import type { VaccinationType } from "@/types/types";
import { Dialog, DialogContent, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { BeakerIcon, CalendarDaysIcon, IdentificationIcon, CheckCircleIcon, XCircleIcon, DocumentTextIcon } from "@heroicons/react/24/outline";

type VaccinationTypeDetailModalProps = {
  vaccinationType: VaccinationType | null;
  isOpen: boolean;
  onClose: () => void;
};

export default function VaccinationTypeDetailModal({ vaccinationType, isOpen, onClose }: VaccinationTypeDetailModalProps) {
  if (!vaccinationType) return null;

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl w-[95vw] sm:w-full max-h-[90vh] overflow-y-auto p-0 bg-linear-to-br from-slate-950 via-indigo-950 to-slate-950 border border-indigo-500/20 shadow-2xl">
        {/* Animated background grid */}
        <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-30">
          <div className="absolute inset-0 bg-[linear-gradient(to_right,#4f46e510_1px,transparent_1px),linear-gradient(to_bottom,#4f46e510_1px,transparent_1px)] bg-size-[4rem_4rem]" />
        </div>
        {/* Glow effects */}
        <div className="absolute inset-0 overflow-hidden pointer-events-none">
          <div className="absolute -top-24 -left-24 w-96 h-96 bg-indigo-600 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-pulse" />
          <div
            className="absolute -bottom-24 -right-24 w-96 h-96 bg-purple-600 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-pulse"
            style={{ animationDelay: "1s" }}
          />
        </div>
        {/* Content */}
        <div className="relative z-10">
          {/* Header with gradient border */}
          <div className="text-center pt-6 sm:pt-8 pb-4 sm:pb-6 px-4 sm:px-8 border-b border-indigo-500/20">
            <div className="inline-flex items-center justify-center w-12 h-12 sm:w-16 sm:h-16 rounded-2xl bg-linear-to-br from-indigo-500 to-purple-600 mb-3 sm:mb-4 shadow-lg shadow-indigo-500/50 relative">
              <div className="absolute inset-0 rounded-2xl bg-linear-to-br from-indigo-400 to-purple-500 opacity-0 group-hover:opacity-100 transition-opacity blur-xl" />
              <BeakerIcon className="w-6 h-6 sm:w-8 sm:h-8 text-white relative z-10" />
            </div>

            <h1 className="text-xl sm:text-2xl md:text-3xl font-bold text-white mb-3">Vaccination Type Details</h1>

            <div className="flex items-center justify-center gap-4 text-sm">
              <span className="flex items-center gap-1.5 text-indigo-300">
                <IdentificationIcon className="w-4 h-4" />
                ID: {vaccinationType.id}
              </span>
            </div>
          </div>

          {/* Body */}
          <div className="p-4 sm:p-6 md:p-8 space-y-4 sm:space-y-6">
            <div className="space-y-4">
              <div className="flex items-center gap-4">
                <BeakerIcon className="w-5 h-5 text-indigo-400" />
                <span className="text-white font-medium">Vaccine Name: {vaccinationType.vaccineName}</span>
              </div>
              <div className="flex items-center gap-4">
                <DocumentTextIcon className="w-5 h-5 text-indigo-400" />
                <span className="text-white font-medium">Description: {vaccinationType.description ?? "N/A"}</span>
              </div>
              <div className="flex items-center gap-4">
                <CalendarDaysIcon className="w-5 h-5 text-indigo-400" />
                <span className="text-white font-medium">Duration: {vaccinationType.durationMonths} months</span>
              </div>
              <div className="flex items-center gap-4">
                {vaccinationType.requiredForAdoption ? (
                  <CheckCircleIcon className="w-5 h-5 text-green-500" />
                ) : (
                  <XCircleIcon className="w-5 h-5 text-red-400" />
                )}
                <span className="text-white font-medium">
                  {vaccinationType.requiredForAdoption ? "Required for Adoption" : "Not Required for Adoption"}
                </span>
              </div>
            </div>
          </div>

          {/* Footer */}
          <DialogFooter className="p-6 border-t border-indigo-500/20">
            <Button variant="secondary" onClick={onClose}>
              Close
            </Button>
          </DialogFooter>
        </div>
      </DialogContent>
    </Dialog>
  );
}
