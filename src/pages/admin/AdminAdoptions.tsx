import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function AdminAdoptions() {
  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-3xl font-bold">Completed Adoptions</h2>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Recent Adoptions</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-muted-foreground">Coming soon...</p>
        </CardContent>
      </Card>
    </div>
  );
}
