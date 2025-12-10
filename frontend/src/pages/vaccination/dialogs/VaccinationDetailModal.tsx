import type { Vaccination, VaccinationType } from "@/types/types";
import { Dialog, DialogContent, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import {
  HeartIcon,
  BeakerIcon,
  CurrencyDollarIcon,
  CalendarDaysIcon,
  IdentificationIcon,
  SparklesIcon,
} from "@heroicons/react/24/outline";
import { HeartIcon as HeartSolid } from "@heroicons/react/24/solid";


type VaccinationDetailModalProps = {
    vaccination: Vaccination | null;
    isOpen: boolean;
    onClose: () => void;
};

export default function VaccinationDetailModal({
    vaccination,
    isOpen,
    onClose,
}: VaccinationDetailModalProps) {
    if (!vaccination) return null;

    const vaccinationTypeName =
        typeof vaccination.vaccinationType === "object"
            ? vaccination.vaccinationType?.vaccineName
            : vaccination.vaccinationType