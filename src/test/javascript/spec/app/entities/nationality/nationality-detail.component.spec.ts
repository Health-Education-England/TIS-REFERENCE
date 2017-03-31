import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {NationalityDetailComponent} from "../../../../../../main/webapp/app/entities/nationality/nationality-detail.component";
import {NationalityService} from "../../../../../../main/webapp/app/entities/nationality/nationality.service";
import {Nationality} from "../../../../../../main/webapp/app/entities/nationality/nationality.model";

describe('Component Tests', () => {

	describe('Nationality Management Detail Component', () => {
		let comp: NationalityDetailComponent;
		let fixture: ComponentFixture<NationalityDetailComponent>;
		let service: NationalityService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [NationalityDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					NationalityService
				]
			}).overrideComponent(NationalityDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(NationalityDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(NationalityService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Nationality(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.nationality).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
