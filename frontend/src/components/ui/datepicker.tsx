import { format } from "date-fns";
import { Calendar as CalendarIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";

interface DatePickerProps {
  id?: string;
  selectedDate: Date | null;
  onDateChange: (date: Date | null) => void;
  className?: string;
}

export function DatePicker({ id, selectedDate, onDateChange, className }: DatePickerProps) {
  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button
          id={id}
          variant="outline"
          className={`w-full justify-start text-left font-normal ${!selectedDate && "text-muted-foreground"} ${className}`}
        >
          <CalendarIcon className="mr-2 h-4 w-4" />
          {selectedDate ? format(selectedDate, "PPP") : <span>Pick a date</span>}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-auto p-0">
        <Calendar
          mode="single"
          selected={selectedDate || undefined}
          onSelect={(date: Date | undefined) => onDateChange(date || null)}
          initialFocus
        />
      </PopoverContent>
    </Popover>
  );
}