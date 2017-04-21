import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {GdcStatus} from "./gdc-status.model";
import {GdcStatusPopupService} from "./gdc-status-popup.service";
import {GdcStatusService} from "./gdc-status.service";

@Component({
	selector: 'jhi-gdc-status-delete-dialog',
	templateUrl: './gdc-status-delete-dialog.component.html'
})
export class GdcStatusDeleteDialogComponent {

	gdcStatus: GdcStatus;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gdcStatusService: GdcStatusService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gdcStatus']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.gdcStatusService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'gdcStatusListModification',
				content: 'Deleted an gdcStatus'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-gdc-status-delete-popup',
	template: ''
})
export class GdcStatusDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gdcStatusPopupService: GdcStatusPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.gdcStatusPopupService
				.open(GdcStatusDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
